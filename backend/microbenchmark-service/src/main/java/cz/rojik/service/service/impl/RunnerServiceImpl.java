package cz.rojik.service.service.impl;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ExecCreation;
import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import cz.rojik.backend.service.BenchmarkStateService;
import cz.rojik.service.constants.OtherConstants;
import cz.rojik.service.constants.ProjectContants;
import cz.rojik.service.dto.ProcessInfoDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.enums.Operation;
import cz.rojik.service.exception.BenchmarkRunException;
import cz.rojik.service.exception.MavenCompileException;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.backend.properties.PathProperties;
import cz.rojik.service.service.RunnerService;
import cz.rojik.service.service.WebSocketService;
import cz.rojik.service.utils.FileUtils;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class RunnerServiceImpl implements RunnerService {

    private static Logger logger = LoggerFactory.getLogger(RunnerServiceImpl.class);

    private static final String REGEX_ERROR = "\\[ERROR\\].*";
    private static final String DOCKER_IMAGE = "docker-microbenchmark";

    @Autowired
    private MessageLogParserServiceImpl messageLogParser;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private BenchmarkStateService benchmarkStateService;

    @Autowired
    private PathProperties pathProperties;

    @Override
    public Set<String> compileProject(String projectId) {
        Set<String> output = new LinkedHashSet<>();
        final Pattern p = Pattern.compile(REGEX_ERROR);

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(pathProperties.getProjects() + projectId + File.separatorChar + ProjectContants.PROJECT_POM));
        request.setGoals(Arrays.asList("clean", "package", "-Dmaven.test.skip=true"));

        Invoker invoker = new DefaultInvoker();
        invoker.setOutputHandler(text -> {
            if (p.matcher(text).matches()) {
                output.add(text);
            }
        });
        try {
            logger.info("Start compiling project {}", projectId);
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            logger.error("Maven invocation exception: {}", e.getMessage());
            throw new MavenCompileException();
        }

        logger.debug("Compiling project {} is completed", projectId);
        return output;
    }

    @Override
    public BenchmarkStateDTO runProject(String projectId, TemplateDTO template, SimpMessageHeaderAccessor socketHeader)
            throws DockerCertificateException, DockerException, InterruptedException, BenchmarkRunException {
        logger.trace("Starting configuration of docker container for project {}", projectId);
        ProcessInfoDTO processInfo;
        BenchmarkStateDTO benchmarkState;
        String error = "";

        final DockerClient client = DefaultDockerClient.fromEnv().build();

        logger.debug("Configuring container for project {}", projectId);
        final ContainerConfig containerConfig = ContainerConfig.builder()
                .image(DOCKER_IMAGE)
                .attachStdout(true)
                .attachStdin(true)
                .tty(true)
                .build();

        final ContainerCreation creation = client.createContainer(containerConfig);
        final String containerId = creation.id();

        logger.info("Start container with ID {} for project {}", containerId, projectId);
        client.startContainer(containerId);
        benchmarkState = updateState(projectId, containerId, BenchmarkStateTypeEnum.BENCHMARK_START);
        logger.debug("Update benchmark state for project {}: {}", projectId, benchmarkState);

        String filePath = pathProperties.getProjects() + projectId + File.separatorChar +
                ProjectContants.TARGET_FOLDER_JAR + ProjectContants.DOCKER_BENCHMARK_FOLDER;
        logger.debug("Get JAR project file {}", filePath);
        try {
            logger.debug("Copy JAR project file to container {} for project {}", containerId, projectId);
            client.copyToContainer(new File(filePath)
                    .toPath(), containerId, OtherConstants.LINUX_FILE_SEPARATOR + ProjectContants.DOCKER_BENCHMARK_FOLDER);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            updateState(projectId, containerId, BenchmarkStateTypeEnum.BENCHMARK_ERROR);
            closeContainer(client, containerId);
            throw new ReadFileException(filePath);
        }

        final String[] command = {"java", "-jar", OtherConstants.LINUX_FILE_SEPARATOR + ProjectContants.DOCKER_BENCHMARK_FOLDER + ProjectContants.GENERATED_PROJECT_JAR};
        final ExecCreation execCreation = client.execCreate(
                containerId, command, DockerClient.ExecCreateParam.attachStdout(),
                DockerClient.ExecCreateParam.attachStderr());

        logger.debug("Run java command for start JAR file in docker container {} for project {}", containerId, projectId);
        final LogStream output = client.execStart(execCreation.id());

        while (output.hasNext()) {
            final String logMessage = StandardCharsets.UTF_8.decode(output.next().content()).toString();
            processInfo = messageLogParser.parseMessage(logMessage, template);
            if (processInfo != null) {
                logger.debug("Benchmark process info from docker container {} for project {}", containerId, projectId);
                if (processInfo.getOperation().equals(Operation.SUCCESS_BENCHMARK)) {
                    benchmarkState = updateState(projectId, containerId, BenchmarkStateTypeEnum.BENCHMARK_SUCCESS);
                    logger.debug("Update benchmark state for project {}: {}", projectId, benchmarkState);
                    copyResultFile(client, containerId, projectId);
                    closeContainer(client, containerId);

                    logger.info("Successful end for benchmark for project {}", projectId);
                    return benchmarkState;
                } else if (processInfo.getOperation().equals(Operation.ERROR_BENCHMARK)) {
                    logger.error("Error in running benchmark: {}", processInfo.getNote());
                    error = processInfo.getNote();
                    break;
                }
                webSocketService.sendProcessInfo(processInfo, socketHeader);
            }
        }

        updateState(projectId, containerId, BenchmarkStateTypeEnum.BENCHMARK_ERROR);
        closeContainer(client, containerId);

        logger.error("Benchmark {} ended in error {}.", projectId, error);
        throw new BenchmarkRunException(projectId, error, FileUtils.readSourceFile(projectId));
    }

    private BenchmarkStateDTO updateState(String projectId, String containerId, BenchmarkStateTypeEnum type) {
        logger.trace("Update benchmark state {} for project {} and container ID", type, projectId, containerId);
        BenchmarkStateDTO state = new BenchmarkStateDTO()
                .setProjectId(projectId)
                .setContainerId(containerId)
                .setType(type);

        state = benchmarkStateService.updateState(state);

        return state;
    }

    private void copyResultFile(DockerClient client, String containerId, String projectId) throws DockerException, InterruptedException {
        logger.trace("Copy result file from docker container {} for project {} to folder.", containerId, projectId);
        File projectsFolder = new File(pathProperties.getResults());
        if (!projectsFolder.exists()) {
            logger.debug("Create results folder in path {}", pathProperties.getResults());
            projectsFolder.mkdirs();
        }

        logger.debug("Start copying result file from docker container {} to disk for project {}", containerId, projectId);
        try (final TarArchiveInputStream tarStream = new TarArchiveInputStream(client.archiveContainer(containerId, ProjectContants.DOCKER_RESULT_FILE))) {
            TarArchiveEntry entry = tarStream.getNextTarEntry();
            File newFile = new File(pathProperties.getResults() + projectId + ProjectContants.JSON_FILE_FORMAT);
            IOUtils.copy(tarStream, new FileOutputStream(newFile));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            updateState(projectId, containerId, BenchmarkStateTypeEnum.BENCHMARK_ERROR);
            closeContainer(client, containerId);
            throw new ReadFileException(ProjectContants.DOCKER_RESULT_FILE);
        }
    }

    private void closeContainer(DockerClient client, String containerId) throws DockerException, InterruptedException {
        logger.debug("Close running docker container {} and remove it.", containerId);
        client.killContainer(containerId);
        client.removeContainer(containerId);
    }

}
