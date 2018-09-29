package cz.rojik.service;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import cz.rojik.dto.ResultDTO;
import cz.rojik.dto.TemplateDTO;

import java.time.LocalDateTime;
import java.util.Set;

public interface RunnerService {

    ResultDTO compileAndStartProject(String projectId, TemplateDTO template, LocalDateTime now);

    Set<String> compileProject(String projectId);

    ResultDTO runProject(String projectId, TemplateDTO template) throws DockerCertificateException, DockerException, InterruptedException;
}
