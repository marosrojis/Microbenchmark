package cz.rojik.service.service;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.BenchmarkRunException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.Set;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface DockerService {

    /**
     * Run benchmark project in docker container
     * @param projectId project id
     * @param template code to measure
     * @param socketHeader user socket session
     * @return project's result (success or error)
     * @throws DockerCertificateException docker's exception
     * @throws DockerException docker's exception
     * @throws InterruptedException docker's exception
     * @throws BenchmarkRunException docker's exception
     */
    BenchmarkStateDTO runProject(String projectId, TemplateDTO template, SimpMessageHeaderAccessor socketHeader)
            throws DockerCertificateException, DockerException, InterruptedException, BenchmarkRunException;

    /**
     * Kill docker container
     * @param containerId docker container to kill
     * @throws cz.rojik.service.exception.DockerException something is wrong with docker
     */
    void killContainer(String containerId) throws cz.rojik.service.exception.DockerException;
}
