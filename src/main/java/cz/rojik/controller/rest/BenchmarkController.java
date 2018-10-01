package cz.rojik.controller.rest;

import cz.rojik.Reader;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.dto.LibrariesToChooseDTO;
import cz.rojik.dto.ProjectDTO;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.exception.ImportsToChooseException;
import cz.rojik.exception.MavenCompileException;
import cz.rojik.service.BenchmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("restBenchmarkController")
@CrossOrigin(origins = "*")
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

        return new ResponseEntity<>(new ProjectDTO(projectId), HttpStatus.OK);
    }

    @PostMapping(MappingURLConstants.BENCHMARK_COMPILE)
    public ResponseEntity<?> compile(@PathVariable String projectId) {
        boolean success = false;
        try {
            success = benchmarkService.compile(projectId);
        } catch (MavenCompileException exception) {
            return new ResponseEntity<>(exception.getErrors(), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(new ProjectDTO(projectId), HttpStatus.OK);
    }
}
