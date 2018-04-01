package cz.rojik;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.HostConfig;
import cz.rojik.constant.ProjectContants;
import cz.rojik.enums.Operation;
import cz.rojik.exception.MavenCompileException;
import cz.rojik.model.ProcessInfo;
import cz.rojik.model.Template;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Runner {

    private static Logger logger = LoggerFactory.getLogger(Runner.class);
    private static final String REGEX_ERROR = "\\[ERROR\\].*";
    private static final String DOCKER_IMAGE = "docker-microbenchmark";

    private MessageLogParser messageLogParser;

    public Runner() {
        this.messageLogParser = new MessageLogParser();
    }

    public Set<String> compileProject(String projectId) {
        Set<String> output = new LinkedHashSet<>();
        ProcessInfo processInfo;
        final Pattern p = Pattern.compile(REGEX_ERROR);

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(ProjectContants.PATH_ALL_PROJECTS + projectId + "/" + ProjectContants.PROJECT_POM));
        request.setGoals( Arrays.asList("clean", "install", "-Dmaven.test.skip=true"));

        Invoker invoker = new DefaultInvoker();
        invoker.setOutputHandler(text -> {
            if (p.matcher(text).matches()) {
                output.add(text);
            }
        });
        try {
            processInfo = new ProcessInfo(Operation.START_COMPILE);
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            processInfo = new ProcessInfo(Operation.ERROR_COMPILE);
            throw new MavenCompileException();
        }

        processInfo = new ProcessInfo(Operation.END_COMPILE);
        return output;
    }

    public boolean runProject(String projectId, Template template) throws DockerCertificateException, DockerException, InterruptedException {
        ProcessInfo processInfo;
        final DockerClient client = DefaultDockerClient.fromEnv().build();

        final HostConfig hostConfig = HostConfig.builder()
                .binds(HostConfig.Bind.from(ProjectContants.PATH_ALL_PROJECTS + projectId + "/" + ProjectContants.TARGET_FOLDER_JAR)
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
        }

        try (final TarArchiveInputStream tarStream = new TarArchiveInputStream(client.archiveContainer(id, ProjectContants.DOCKER_RESULT_FILE))) {
            TarArchiveEntry entry = tarStream.getNextTarEntry();
            File newFile = new File(ProjectContants.PATH_RESULT + projectId + ProjectContants.JSON_FILE_FORMAT);
            IOUtils.copy(tarStream, new FileOutputStream(newFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.killContainer(id);
        client.removeContainer(id);

        return true;
    }

}
