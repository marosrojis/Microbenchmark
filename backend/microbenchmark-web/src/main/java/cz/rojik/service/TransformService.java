package cz.rojik.service;

import cz.rojik.backend.dto.ResultDTO;
import cz.rojik.service.dto.TemplateDTO;

public interface TransformService {

    ResultDTO createResult(String projectId, TemplateDTO template, cz.rojik.service.dto.ResultDTO benchmarkResult);

}
