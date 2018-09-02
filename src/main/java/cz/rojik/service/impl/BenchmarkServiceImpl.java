package cz.rojik.service.impl;

import cz.rojik.dto.ResultDTO;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.service.BenchmarkService;
import cz.rojik.service.GeneratorService;
import cz.rojik.service.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BenchmarkServiceImpl implements BenchmarkService {

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private RunnerService runnerService;

    @Override
    public ResultDTO runBenchmark(TemplateDTO template) {
        LocalDateTime now = LocalDateTime.now();
        String projectId = generatorService.generateJavaClass(template);

        ResultDTO result = runnerService.compileAndStartProject(projectId, template, now);
        return result;
    }
}
