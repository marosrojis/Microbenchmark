package cz.rojik.backend.service;

import cz.rojik.backend.dto.BenchmarkDTO;

import java.util.List;
import java.util.Optional;

public interface BenchmarkService {

    BenchmarkDTO getOne(Long id);

    List<BenchmarkDTO> getAll(Optional<Boolean> success);

    BenchmarkDTO saveResult(BenchmarkDTO result);

    BenchmarkDTO delete(Long id);

    BenchmarkDTO assignToUser(Long id, Long userId);
}
