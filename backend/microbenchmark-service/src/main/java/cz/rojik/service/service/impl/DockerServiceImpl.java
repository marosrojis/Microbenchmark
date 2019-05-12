package cz.rojik.service.service.impl;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.ContainerNotFoundException;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.exceptions.DockerRequestException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
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
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.backend.properties.PathProperties;
import cz.rojik.service.service.DockerService;
import cz.rojik.service.service.WebSocketService;
import cz.rojik.service.utils.FileUtils;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class DockerServiceImpl implements DockerService {

    private static Logger LOGGER = LoggerFactory.getLogger(DockerServiceImpl.class);

    private static final String DOCKER_IMAGE = "docker-microbenchmark";
    private static final int ITERATIONS_OF_PART = 4;

    @Autowired
    private MessageLogParserServiceImpl messageLogParser;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private BenchmarkStateService benchmarkStateService;

    @Autowired
    private PathProperties pathProperties;

    @Override
    public BenchmarkStateDTO runProject(String projectId, TemplateDTO template, SimpMessageHeaderAccessor socketHeader)
            throws DockerCertificateException, DockerException, InterruptedException, BenchmarkRunException {
        LOGGER.trace("Starting configuration of docker container for project {}", projectId);
        ProcessInfoDTO processInfo;
        BenchmarkStateDTO benchmarkState;
        String error = "Problem with running benchmark.";
        int currentIteration = 0;
        int totalIterations = template.getTestMethods().size() * (template.getMeasurement() + template.getWarmup());

        final DockerClient client = DefaultDockerClient.fromEnv().build();

        LOGGER.debug("Configuring container for project {}", projectId);
        final ContainerConfig containerConfig = ContainerConfig.builder()
                .image(DOCKER_IMAGE)
                .attachStdout(true)
                .attachStdin(true)
                .tty(true)
                .networkDisabled(true)
                .build();

        final ContainerCreation creation = client.createContainer(containerConfig);
        final String containerId = creation.id();

        LOGGER.info("Start container with ID {} for project {}", containerId, projectId);
        client.startContainer(containerId);
        benchmarkState = updateState(projectId, containerId, BenchmarkStateTypeEnum.BENCHMARK_START);
        LOGGER.debug("Update benchmark state for project {}: {}", projectId, benchmarkState);

        String filePath = pathProperties.getProjects() + projectId + File.separatorChar +
                ProjectContants.TARGET_FOLDER_JAR + ProjectContants.DOCKER_BENCHMARK_FOLDER;
        LOGGER.debug("Get JAR project file {}", filePath);
        try {
            LOGGER.debug("Copy JAR project file to container {} for project {}", containerId, projectId);
            client.copyToContainer(new File(filePath)
                    .toPath(), containerId, OtherConstants.LINUX_FILE_SEPARATOR + ProjectContants.DOCKER_BENCHMARK_FOLDER);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            updateState(projectId, containerId, BenchmarkStateTypeEnum.BENCHMARK_ERROR);
            killContainer(containerId);
            throw new ReadFileException(filePath, e);
        }

        final String[] command = {"java", "-jar", OtherConstants.LINUX_FILE_SEPARATOR + ProjectContants.DOCKER_BENCHMARK_FOLDER + ProjectContants.GENERATED_PROJECT_JAR};
        final ExecCreation execCreation = client.execCreate(
                containerId, command, DockerClient.ExecCreateParam.attachStdout(),
                DockerClient.ExecCreateParam.attachStderr());

        LOGGER.debug("Run java command for start JAR file in docker container {} for project {}", containerId, projectId);
        final LogStream output = client.execStart(execCreation.id());

        while (output.hasNext()) {
            final String logMessage = StandardCharsets.UTF_8.decode(output.next().content()).toString();
            processInfo = messageLogParser.parseMessage(logMessage, template, projectId);
            if (processInfo != null) {
                LOGGER.debug("Benchmark process info from docker container {} for project {} (iteration {})", containerId, projectId, currentIteration);
                if (processInfo.getOperation().equals(Operation.SUCCESS_BENCHMARK)) {
                    benchmarkState = updateState(projectId, containerId, BenchmarkStateTypeEnum.BENCHMARK_SUCCESS);
                    LOGGER.debug("Update benchmark state for project {}: {}", projectId, benchmarkState);
                    copyResultFile(client, containerId, projectId);
                    killContainer(containerId);

                    LOGGER.info("Successful end for benchmark for project {}", projectId);
                    return benchmarkState;
                } else if (processInfo.getOperation().equals(Operation.ERROR_BENCHMARK)) {
                    LOGGER.error("Error in running benchmark: {}", processInfo.getNote());
                    error = processInfo.getNote();

                    updateState(projectId, containerId, BenchmarkStateTypeEnum.BENCHMARK_ERROR);
                    killContainer(containerId);
                    break;
                }

                benchmarkState = updateRunningState(totalIterations, currentIteration, benchmarkState);
                processInfo.setEstimatedEndTime(benchmarkState.getEstimatedEndTime());
                webSocketService.sendProcessInfo(processInfo, socketHeader);
                if (!processInfo.getOperation().equals(Operation.RESULT)) {
                    currentIteration++;
                }
            }
        }

        LOGGER.error("Benchmark {} ended in error {}.", projectId, error);
        throw new BenchmarkRunException(projectId, error, FileUtils.readSourceFile(projectId));
    }

    @Override
    public void killContainer(String containerId) throws cz.rojik.service.exception.DockerException {
        LOGGER.debug("Kill running docker container {} and remove it.", containerId);
        final DockerClient client;
        try {
            client = DefaultDockerClient.fromEnv().build();
            ContainerInfo containerInfo = client.inspectContainer(containerId);
            if (!containerInfo.state().running()) {
                LOGGER.trace("Container {} is not running.", containerId);
                return;
            }
            client.killContainer(containerId);
            client.removeContainer(containerId);
        } catch (ContainerNotFoundException | DockerRequestException e) {
            LOGGER.debug(e.getMessage(), containerId);
            return;
        } catch (DockerCertificateException | InterruptedException | DockerException e) {
            LOGGER.error("Docker problem.", e);
            throw new cz.rojik.service.exception.DockerException(e.getMessage(), e);
        }
        LOGGER.debug("Killing running docker container {} was successful", containerId);
    }

    /**
     * Copy generated json file with benchmark result to folder in project
     * @param client docker client
     * @param containerId running docker container ID
     * @param projectId project ID
     * @throws DockerException
     * @throws InterruptedException
     */
    private void copyResultFile(DockerClient client, String containerId, String projectId) throws DockerException, InterruptedException {
        LOGGER.trace("Copy result file from docker container {} for project {} to folder.", containerId, projectId);
        File projectsFolder = new File(pathProperties.getResults());
        if (!projectsFolder.exists()) {
            LOGGER.debug("Create results folder in path {}", pathProperties.getResults());
            projectsFolder.mkdirs();
        }

        LOGGER.debug("Start copying result file from docker container {} to disk for project {}", containerId, projectId);
        try (final TarArchiveInputStream tarStream = new TarArchiveInputStream(client.archiveContainer(containerId, ProjectContants.DOCKER_RESULT_FILE))) {
            TarArchiveEntry entry;
            File newFile = new File(pathProperties.getResults() + projectId + ProjectContants.JSON_FILE_FORMAT);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            while ((entry = tarStream.getNextTarEntry()) != null) {
                IOUtils.copy(tarStream, fileOutputStream);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            updateState(projectId, containerId, BenchmarkStateTypeEnum.BENCHMARK_ERROR);
            killContainer(containerId);
            throw new ReadFileException(ProjectContants.DOCKER_RESULT_FILE);
        }
    }

    /**
     * Update benchmark state in database for running project
     * @param projectId running project ID
     * @param containerId docker container ID
     * @param type type of benchmark state
     * @return updated {@link BenchmarkStateDTO}
     */
    private BenchmarkStateDTO updateState(String projectId, String containerId, BenchmarkStateTypeEnum type) {
        LOGGER.trace("Update benchmark state {} for project {} and container ID", type, projectId, containerId);
        BenchmarkStateDTO state = new BenchmarkStateDTO()
                .setProjectId(projectId)
                .setContainerId(containerId)
                .setEstimatedEndTime(null)
                .setType(type);

        if (type.equals(BenchmarkStateTypeEnum.BENCHMARK_START)) {
            state.setCompleted(0);
        }
        else {
            state.setCompleted(100);
        }

        state = benchmarkStateService.updateState(state);

        return state;
    }

    /**
     * Calculate estimate time of running project.
     * Update running benchmark state with percent of completed project and estimated time.
     * @param totalIterations total project iterations
     * @param currentIteration current running iteration
     * @param state last updated benchmark state
     * @return updated {@link BenchmarkStateDTO}
     */
    private BenchmarkStateDTO updateRunningState(int totalIterations, int currentIteration, BenchmarkStateDTO state) {
        if (currentIteration % ITERATIONS_OF_PART != 0 || currentIteration == 0) {
            return state;
        }
        LOGGER.trace("Update benchmark state {} for project {} and container ID", BenchmarkStateTypeEnum.BENCHMARK_RUNNING, state.getProjectId(), state.getContainerId());

        int percent = 100 * currentIteration / totalIterations;

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(state.getUpdated(), now);
        long timeOfOneIteration = duration.getSeconds() / ITERATIONS_OF_PART;
        long restOfTime = timeOfOneIteration * (totalIterations - currentIteration);

        LocalDateTime estimatedEndTime = now.plusSeconds(restOfTime);

        LOGGER.trace("Rest of time of benchmark {} is {}", state.getProjectId(), estimatedEndTime);

        state.setType(BenchmarkStateTypeEnum.BENCHMARK_RUNNING)
                .setCompleted(percent)
                .setUpdated(LocalDateTime.now())
                .setEstimatedEndTime(estimatedEndTime);

        state = benchmarkStateService.updateState(state);
        return state;
    }

}
