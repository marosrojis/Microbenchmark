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
     * Get benchmark by ID
     * @param id benchmark id
     * @return benchmark
     */
    @GetMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity<BenchmarkDTO> getOne(@PathVariable Long id) {
        BenchmarkDTO benchmark = benchmarkService.getOne(id);
        return new ResponseEntity<>(benchmark, HttpStatus.OK);
    }

    /**
     * Get all benchmarks depends on the specified parameters.
     * If user has roles 'USER' or 'DEMO', returns only his benchmarks.
     * If user has role 'ADMIN', returns all benchmarks.
     * @param success optional parameter. If is true return only successful benchmarks, if false return only unsuccessful benchmarks
     * @param user optional parameter. Return benchmarks that were created by the specific user
     * @return list of benchmarks
     */
    @GetMapping
    public ResponseEntity<List<BenchmarkDTO>> getAll(@RequestParam(value = "success") Optional<Boolean> success,
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
    @PostMapping(MappingURLConstants.BENCHMARK_ASSIGN_TO_USER)
    public ResponseEntity<BenchmarkDTO> assignToUser(@PathVariable Long id, @PathVariable Long userId) {
        BenchmarkDTO benchmark = benchmarkService.assignToUser(id, userId);
        return new ResponseEntity<>(benchmark, HttpStatus.OK);
    }

    /**
     * Delete specific benchmark.
     * @param id benchmark id to delete
     */
    @DeleteMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity delete(@PathVariable Long id) {
        benchmarkService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
