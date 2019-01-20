package cz.rojik.service;

import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.service.dto.TemplateDTO;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface TransformService {

    /**
     * Create {@link BenchmarkDTO} object with data from {@link TemplateDTO} and generated result {@link cz.rojik.service.dto.ResultDTO} from JMH project.
     * @param projectId run project ID
     * @param template code from user to measure
     * @param benchmarkResult result of successful completed benchmark
     * @return {@link BenchmarkDTO} object with codes and successful result
     */
    BenchmarkDTO createResult(String projectId, TemplateDTO template, cz.rojik.service.dto.ResultDTO benchmarkResult);

    /**
     * Create {@link BenchmarkDTO} object with data from {@link TemplateDTO} and occurred errors which are thrown during benchmark (e. g. runtime exception).
     * @param projectId run project ID
     * @param template code from user to measure
     * @param error error occurred during benchmark
     * @return {@link BenchmarkDTO} object with codes and unsuccessful result (errors).
     */
    BenchmarkDTO createErrorResult(String projectId, TemplateDTO template, String error);

}
