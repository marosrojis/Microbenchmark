package cz.rojik.controller.rest;

import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.constants.MappingURLConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cz.rojik.service.dto.LibrariesDTO;
import cz.rojik.service.dto.LibrariesToChooseDTO;
import cz.rojik.service.dto.ProjectDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.ImportsToChooseException;
import cz.rojik.service.exception.MavenCompileException;
import cz.rojik.service.service.BenchmarkService;

import java.util.List;
import java.util.Optional;

@RestController("restBenchmarkController")
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.BENCHMARK)
public class BenchmarkController {

    private static Logger logger = LoggerFactory.getLogger(BenchmarkController.class);

    @Autowired
    private BenchmarkService benchmarkService;

    @Autowired
    private cz.rojik.backend.service.BenchmarkService benchmarkBackendService;

    @GetMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity<BenchmarkDTO> getOne(@PathVariable Long id) {
        BenchmarkDTO benchmark = benchmarkBackendService.getOne(id);
        return new ResponseEntity<>(benchmark, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BenchmarkDTO>> getAll(@RequestParam(value = "success") Optional<Boolean> success) {
        List<BenchmarkDTO> benchmarks = benchmarkBackendService.getAll(success);
        return new ResponseEntity<>(benchmarks, HttpStatus.OK);
    }

    @PostMapping(MappingURLConstants.BENCHMARK_CREATE)
    public ResponseEntity<?> create(@RequestBody TemplateDTO template) {
        String projectId = "";

        try {
            projectId = benchmarkService.createProject(template);
        } catch (ImportsToChooseException exception) {
            LibrariesToChooseDTO libraries = new LibrariesToChooseDTO(exception.getProjectId(), exception.getImportsToChoose());
            logger.debug("Return response CONFLICT - select libraries to import {} in project {}", libraries.getImports(), libraries.getProjectId());
            return new ResponseEntity<>(libraries, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(new ProjectDTO(projectId), HttpStatus.OK);
    }

    @PostMapping(MappingURLConstants.BENCHMARK_IMPORT_LIBRARIES)
    public ResponseEntity<ProjectDTO> importLibraries(@RequestBody LibrariesDTO libraries) {
        String projectId = benchmarkService.importLibraries(libraries);

        return new ResponseEntity<>(new ProjectDTO(projectId), HttpStatus.OK);
    }

    @PostMapping(MappingURLConstants.BENCHMARK_COMPILE)
    public ResponseEntity<?> compile(@PathVariable String projectId) {
        try {
            benchmarkService.compile(projectId);
        } catch (MavenCompileException exception) {
            logger.debug("Return response BAD_REQUEST - compiler errors {} in project {}: {}", exception.getErrors(), projectId, exception.getMessage());
            return new ResponseEntity<>(exception.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ProjectDTO(projectId), HttpStatus.OK);
    }

    @PostMapping(MappingURLConstants.BENCHMARK_ASSIGN_TO_USER)
    public ResponseEntity<BenchmarkDTO> assignToUser(@PathVariable Long id, @PathVariable Long userId) {
        BenchmarkDTO benchmark = benchmarkBackendService.assignToUser(id, userId);
        return new ResponseEntity<>(benchmark, HttpStatus.OK);
    }

    @DeleteMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity delete(@PathVariable Long id) {
        benchmarkBackendService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
