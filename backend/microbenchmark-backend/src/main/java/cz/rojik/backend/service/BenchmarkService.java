package cz.rojik.backend.service;

import cz.rojik.backend.dto.BenchmarkDTO;

import java.util.List;
import java.util.Optional;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface BenchmarkService {

    /**
     * Get benchmark based on ID
     * @param id benchmark's ID
     * @return benchmark object
     */
    BenchmarkDTO getOne(Long id);

    /**
     * Get list of benchmarks based on optional parameters and permissions of logged user.
     * @param success Optional parameter to get all success/unsuccess
     * @param user Optional parameter to get benchmarks only for specific user
     * @return list of benchmarks
     */
    List<BenchmarkDTO> getAll(Optional<Boolean> success, Optional<Long> user);

    /**
     * Save benchmark to database
     * @param result benchmark to save
     * @return saved object to database
     */
    BenchmarkDTO saveResult(BenchmarkDTO result);

    /**
     * Delete benchmark from database
     * @param id benchmark's id to delete
     * @return deleted benchmark
     */
    BenchmarkDTO delete(Long id);

    /**
     * Method for assign benchmark to specific user
     * @param id benchmark's id
     * @param userId user's id
     * @return benchmark assigned to user
     */
    BenchmarkDTO assignToUser(Long id, Long userId);
}
