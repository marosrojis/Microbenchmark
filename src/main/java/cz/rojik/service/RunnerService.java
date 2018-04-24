package cz.rojik.service;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import cz.rojik.dto.Result;
import cz.rojik.dto.Template;

import java.time.LocalDateTime;
import java.util.Set;

public interface RunnerService {

    Result compileAndStartProject(String projectId, Template template, LocalDateTime now);

    Set<String> compileProject(String projectId);

    boolean runProject(String projectId, Template template) throws DockerCertificateException, DockerException, InterruptedException;
}
