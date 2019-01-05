package cz.rojik.backend.service;

import cz.rojik.backend.dto.BenchmarkDTO;

import java.util.List;
import java.util.Optional;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface BenchmarkService {

    BenchmarkDTO getOne(Long id);

    List<BenchmarkDTO> getAll(Optional<Boolean> success, Optional<Long> user);

    BenchmarkDTO saveResult(BenchmarkDTO result);

    BenchmarkDTO delete(Long id);

    BenchmarkDTO assignToUser(Long id, Long userId);
}
