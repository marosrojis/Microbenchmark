package cz.rojik.service;

import cz.rojik.dto.ResultDTO;
import cz.rojik.dto.TemplateDTO;

public interface BenchmarkService {

    ResultDTO runBenchmark(TemplateDTO template);
}
