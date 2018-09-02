package cz.rojik.controller;

import cz.rojik.Reader;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.dto.ResultDTO;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.service.BenchmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MappingURLConstants.BENCHMARK)
public class BenchmarkController {

    @Autowired
    private BenchmarkService benchmarkService;

    @RequestMapping(MappingURLConstants.BENCHMARK_RUN)
    public ResultDTO run() {
        Reader reader = new Reader();
        TemplateDTO input = reader.readInputs();

        ResultDTO result = benchmarkService.runBenchmark(input);
        return result;
    }
}
