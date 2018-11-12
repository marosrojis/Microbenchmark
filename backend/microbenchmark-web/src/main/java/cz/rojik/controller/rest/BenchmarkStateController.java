package cz.rojik.controller.rest;

import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.service.BenchmarkStateService;
import cz.rojik.constants.MappingURLConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.BENCHMARK_STATE)
public class BenchmarkStateController {

    @Autowired
    private BenchmarkStateService benchmarkStateService;

    @GetMapping
    public ResponseEntity<List<BenchmarkStateDTO>> getAllByState(@RequestParam(value = "running") Optional<Boolean> running) {
        List<BenchmarkStateDTO> benchmarksState = benchmarkStateService.getBenchmarksState(running);
        return new ResponseEntity<>(benchmarksState, HttpStatus.OK);
    }
}
