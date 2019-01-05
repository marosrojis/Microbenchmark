package cz.rojik.service;

import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.service.dto.TemplateDTO;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface TransformService {

    BenchmarkDTO createResult(String projectId, TemplateDTO template, cz.rojik.service.dto.ResultDTO benchmarkResult);

    BenchmarkDTO createErrorResult(String projectId, TemplateDTO template, String error);

}
