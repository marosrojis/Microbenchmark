package cz.rojik.controller.rest;

import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.service.BenchmarkService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController("restBenchmarkController")
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.BENCHMARK)
public class BenchmarkController {

    private static Logger logger = LoggerFactory.getLogger(BenchmarkController.class);

    @Autowired
    private BenchmarkService benchmarkService;

    @GetMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity<BenchmarkDTO> getOne(@PathVariable Long id) {
        BenchmarkDTO benchmark = benchmarkService.getOne(id);
        return new ResponseEntity<>(benchmark, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BenchmarkDTO>> getAll(@RequestParam(value = "success") Optional<Boolean> success) {
        List<BenchmarkDTO> benchmarks = benchmarkService.getAll(success);
        return new ResponseEntity<>(benchmarks, HttpStatus.OK);
    }

    @PostMapping(MappingURLConstants.BENCHMARK_ASSIGN_TO_USER)
    public ResponseEntity<BenchmarkDTO> assignToUser(@PathVariable Long id, @PathVariable Long userId) {
        BenchmarkDTO benchmark = benchmarkService.assignToUser(id, userId);
        return new ResponseEntity<>(benchmark, HttpStatus.OK);
    }

    @DeleteMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity delete(@PathVariable Long id) {
        benchmarkService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
