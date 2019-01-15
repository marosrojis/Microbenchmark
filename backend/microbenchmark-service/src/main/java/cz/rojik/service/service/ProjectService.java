package cz.rojik.service.service;

import cz.rojik.service.dto.LibrariesDTO;
import cz.rojik.service.dto.ResultDTO;
import cz.rojik.service.exception.BenchmarkRunException;
import cz.rojik.service.exception.ImportsToChooseException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import cz.rojik.service.dto.TemplateDTO;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface ProjectService {

    /**
     * Generate complete JMH project with unique ID.
     * @param template code from user to measure
     * @return unique project ID
     * @throws ImportsToChooseException Code needs to choose from which package want to import class
     */
    String createProject(TemplateDTO template) throws ImportsToChooseException;

    /**
     * Set selected libraries to project and generate complete JMH class
     * @param libraries selected libraries from user
     * @return unique project ID
     */
    String importLibraries(LibrariesDTO libraries);

    /**
     * Compile generated JMH project with maven plugin
     * @param projectId unique project ID
     * @return unique project ID
     */
    boolean compile(String projectId);

    /**
     * Run generated JMH project, start measure microbenchmark in docker container
     * @param projectId project to measure
     * @param template code to measure from user
     * @param socketHeader session to send socket
     * @return result of microbenchmark
     * @throws BenchmarkRunException exception during measuring benchmark
     */
    ResultDTO runBenchmark(String projectId, TemplateDTO template, SimpMessageHeaderAccessor socketHeader) throws BenchmarkRunException;

    /**
     * Kill docker container with running project
     * @param projectId project to kill
     */
    void kill(String projectId);

}
