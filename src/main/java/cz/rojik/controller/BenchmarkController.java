package cz.rojik.controller;

import cz.rojik.Reader;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.dto.LibrariesToChooseDTO;
import cz.rojik.dto.ResultDTO;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.exception.ImportsToChooseException;
import cz.rojik.service.BenchmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MappingURLConstants.BENCHMARK)
public class BenchmarkController {

    @Autowired
    private BenchmarkService benchmarkService;

    @PostMapping(MappingURLConstants.BENCHMARK_CREATE)
    public ResponseEntity<?> createProject(@RequestBody TemplateDTO template) {
        String projectId = "";

        try {
            projectId = benchmarkService.createProject(template);
        } catch (ImportsToChooseException exception) {
            LibrariesToChooseDTO libraries = new LibrariesToChooseDTO(exception.getProjectId(), exception.getImportsToChoose());
            return new ResponseEntity<>(libraries, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(projectId, HttpStatus.OK);
    }

    @GetMapping(MappingURLConstants.BENCHMARK_RUN)
    public ResultDTO run() {
        Reader reader = new Reader();
        TemplateDTO input = reader.readInputs();

        ResultDTO result = benchmarkService.runBenchmark(input);
        return result;
    }

    @RequestMapping(MappingURLConstants.BENCHMARK_COMPILE)
    public ResponseEntity<String> compile() {
//        ResultDTO result = benchmarkService.runBenchmark(input);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
