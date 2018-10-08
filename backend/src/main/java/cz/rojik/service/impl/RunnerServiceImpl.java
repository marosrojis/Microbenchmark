package cz.rojik.service.impl;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.HostConfig;
import cz.rojik.constants.ProjectContants;
import cz.rojik.dto.ResultDTO;
import cz.rojik.enums.Operation;
import cz.rojik.exception.MavenCompileException;
import cz.rojik.dto.ProcessInfoDTO;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.service.RunnerService;
import cz.rojik.service.WebSocketService;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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

    @Override
    public ResultDTO compileAndStartProject(String projectId, TemplateDTO template, LocalDateTime now) {
//        GeneratorHTML generatorHTML = new GeneratorHTML();
//
//        ResultDTO result;
//        Set<String> errors = compileProject(projectId);
//
//        if (errors.size() == 0) {
//            try {
//                runProject(projectId, template);
//            } catch (DockerCertificateException | DockerException | InterruptedException e) {
//                logger.error("Throw exception during run docker container with project.");
//            }
//            result = resultParserService.parseResult(projectId);
//            generatorHTML.generateHTMLFile(result, projectId, template);
//        }
//        else {
//            ProcessInfoDTO processInfo = new ProcessInfoDTO(Operation.ERROR_COMPILE);
//
//            List<ErrorDTO> errorList = errorsParserService.getSyntaxErrors(errors);
//            List<ErrorInfoDTO> errorInfoList = errorsParserService.processErrorList(errorList, projectId);
//
//            result = new ResultDTO(false)
//                    .setErrors(errorInfoList);
//            generatorHTML.generateHTMLFile(result, projectId, template);
//        }
//
//        return result;
        return null;
    }

    @Override
    public Set<String> compileProject(String projectId) {
        Set<String> output = new LinkedHashSet<>();
        ProcessInfoDTO processInfo;
        final Pattern p = Pattern.compile(REGEX_ERROR);

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(ProjectContants.PROJECTS_FOLDER + projectId + "/" + ProjectContants.PROJECT_POM));
        request.setGoals( Arrays.asList("clean", "install", "-Dmaven.test.skip=true"));

        Invoker invoker = new DefaultInvoker();
        invoker.setOutputHandler(text -> {
            if (p.matcher(text).matches()) {
                output.add(text);
            }
        });
        try {
            processInfo = new ProcessInfoDTO(Operation.START_COMPILE);
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            processInfo = new ProcessInfoDTO(Operation.ERROR_COMPILE);
            throw new MavenCompileException();
        }

        processInfo = new ProcessInfoDTO(Operation.END_COMPILE);
        return output;
    }

    @Override
    public String runProject(String projectId, TemplateDTO template, SimpMessageHeaderAccessor socketHeader) throws DockerCertificateException, DockerException, InterruptedException {
        ProcessInfoDTO processInfo;
        final DockerClient client = DefaultDockerClient.fromEnv().build();

        final HostConfig hostConfig = HostConfig.builder()
                .binds(HostConfig.Bind.from(System.getProperty("user.dir") + "/" + ProjectContants.PROJECTS_FOLDER + projectId + "/" + ProjectContants.TARGET_FOLDER_JAR)
                        .to(ProjectContants.DOCKER_SHARED_FOLDER)
                        .readOnly(true)
                        .build())
                .build();

        final ContainerConfig containerConfig = ContainerConfig.builder()
                .hostConfig(hostConfig)
                .image(DOCKER_IMAGE)
                .attachStdout(true)
                .attachStdin(true)
                .tty(true)
                .build();

        final ContainerCreation creation = client.createContainer(containerConfig);
        final String id = creation.id();

        client.startContainer(id);

        final String[] command = {"java", "-jar", ProjectContants.DOCKER_SHARED_FOLDER + ProjectContants.GENERATED_PROJECT_JAR};
        final ExecCreation execCreation = client.execCreate(
                id, command, DockerClient.ExecCreateParam.attachStdout(),
                DockerClient.ExecCreateParam.attachStderr());
        final LogStream output = client.execStart(execCreation.id());

        while (output.hasNext()) {
            final String logMessage = StandardCharsets.UTF_8.decode(output.next().content()).toString();
            processInfo = messageLogParser.parseMessage(logMessage, template);
            if (processInfo != null) {
                webSocketService.sendProcessInfo(processInfo, socketHeader);
            }
        }

//        Process p;
//        try {
//            p = Runtime.getRuntime().exec("docker cp " + id + ":/benchmark/result/results.json " +
//                    ProjectContants.PATH_RESULT + projectId + ProjectContants.JSON_FILE_FORMAT); // TODO: try copy result file via docker-client library (below commented code)
//            p.waitFor();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try (final TarArchiveInputStream tarStream = new TarArchiveInputStream(client.archiveContainer(id, ProjectContants.DOCKER_RESULT_FILE))) {
            TarArchiveEntry entry = tarStream.getNextTarEntry();
            File newFile = new File(ProjectContants.PATH_RESULT + projectId + ProjectContants.JSON_FILE_FORMAT);
            IOUtils.copy(tarStream, new FileOutputStream(newFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.killContainer(id);
        client.removeContainer(id);

        return projectId;
    }

}
