package cz.rojik.service;

import cz.rojik.dto.ResultDTO;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.exception.ImportsToChooseException;

public interface BenchmarkService {

    String createProject(TemplateDTO template) throws ImportsToChooseException;

    ResultDTO runBenchmark(TemplateDTO template);
}
