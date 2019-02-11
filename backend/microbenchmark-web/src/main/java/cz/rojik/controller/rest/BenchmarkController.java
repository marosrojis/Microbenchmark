package cz.rojik.controller.rest;

import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.service.BenchmarkService;
import cz.rojik.constants.MappingURLConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

/**
 * Controller for benchmark manipulation.
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@RestController("restBenchmarkController")
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.BENCHMARK)
public class BenchmarkController {

    private static Logger LOGGER = LoggerFactory.getLogger(BenchmarkController.class);

    @Autowired
    private BenchmarkService benchmarkService;

    /**
     * Get a benchmark with an ID
     * @param id benchmark id
     * @return benchmark
     */
    @ApiOperation(value = "Get a benchmark with an ID", notes = "This can only be done by the logged in user.", response = BenchmarkDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the benchmark"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity<BenchmarkDTO> getOne(@ApiParam(value = "benchmark ID", required = true) @PathVariable Long id) {
        BenchmarkDTO benchmark = benchmarkService.getOne(id);
        return new ResponseEntity<>(benchmark, HttpStatus.OK);
    }

    /**
     * Get a list of benchmarks depends on the specified parameters.
     * If user has roles 'USER' or 'DEMO', returns only his benchmarks.
     * If user has role 'ADMIN', returns all benchmarks.
     * @param success optional parameter. If is true return only successful benchmarks, if false return only unsuccessful benchmarks
     * @param user optional parameter. Return benchmarks that were created by the specific user
     * @return list of benchmarks
     */
    @ApiOperation(value = "Get a list of benchmarks depends on the specified parameter", notes = "This can only be done by the logged in user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the list of benchmarks"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<List<BenchmarkDTO>> getAll(@ApiParam(value = "optional parameter. If is true return only successful benchmarks, if false return only unsuccessful benchmarks")
                                                         @RequestParam(value = "success") Optional<Boolean> success,
                                                     @ApiParam(value = "optional parameter. Return benchmarks that were created by the specific user")
                                                        @RequestParam(value = "user") Optional<Long> user) {
        List<BenchmarkDTO> benchmarks = benchmarkService.getAll(success, user);
        return new ResponseEntity<>(benchmarks, HttpStatus.OK);
    }

    /**
     * Assign specific benchmark to another user.
     * @param id benchmark id
     * @param userId user id
     * @return benchmark with new user
     */
    @ApiOperation(value = "Assign specific benchmark to another user", notes = "This can only be done by the logged in user with ADMIN role.", response = BenchmarkDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully modified the benchmark"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PostMapping(MappingURLConstants.BENCHMARK_ASSIGN_TO_USER)
    public ResponseEntity<BenchmarkDTO> assignToUser(@ApiParam(value = "benchmark ID", required = true) @PathVariable Long id,
                                                     @ApiParam(value = "user ID", required = true) @PathVariable Long userId) {
        BenchmarkDTO benchmark = benchmarkService.assignToUser(id, userId);
        return new ResponseEntity<>(benchmark, HttpStatus.OK);
    }

    /**
     * Delete specific benchmark.
     * @param id benchmark id to delete
     */
    @ApiOperation(value = "Delete specific benchmark", notes = "This can only be done by the logged in user with ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the benchmark"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @DeleteMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity delete(@ApiParam(value = "benchmark ID", required = true) @PathVariable Long id) {
        benchmarkService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
