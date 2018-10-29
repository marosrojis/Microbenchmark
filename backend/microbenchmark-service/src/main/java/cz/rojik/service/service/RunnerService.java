package cz.rojik.service.service;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import cz.rojik.service.dto.TemplateDTO;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.Set;

public interface RunnerService {

    Set<String> compileProject(String projectId);

    String runProject(String projectId, TemplateDTO template, SimpMessageHeaderAccessor socketHeader) throws DockerCertificateException, DockerException, InterruptedException;
}
