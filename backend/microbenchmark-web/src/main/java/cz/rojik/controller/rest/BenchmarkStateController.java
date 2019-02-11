package cz.rojik.controller.rest;

import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.service.BenchmarkStateService;
import cz.rojik.constants.MappingURLConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

/**
 * Controller for benchmark state manipulation.
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.BENCHMARK_STATE)
public class BenchmarkStateController {

    @Autowired
    private BenchmarkStateService benchmarkStateService;

    /**
     * Return info about what benchmark has run, who has started benchmark and another info.
     * @param running if true return info only about running benchmarks, if false return info only about completed benchmarks.
     * @return list of benchmarks with basic info
     */
    @ApiOperation(value = "Return info about what benchmark has run, who has started benchmark and another info", notes = "This can only be done by the logged in user with ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the list of benchmark states"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<List<BenchmarkStateDTO>> getAll(@ApiParam(value = "optional parameter. If true return info only about running benchmarks, if false return info only about completed benchmarks")
                                                              @RequestParam(value = "running") Optional<Boolean> running) {
        List<BenchmarkStateDTO> benchmarksState = benchmarkStateService.getBenchmarksState(running);
        return new ResponseEntity<>(benchmarksState, HttpStatus.OK);
    }
}
