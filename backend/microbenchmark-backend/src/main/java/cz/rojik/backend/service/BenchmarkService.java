package cz.rojik.backend.service;

import cz.rojik.backend.dto.BenchmarkDTO;

import java.util.List;

public interface BenchmarkService {

    BenchmarkDTO getOne(Long id);

    List<BenchmarkDTO> getAll();

    BenchmarkDTO saveResult(BenchmarkDTO result);

    BenchmarkDTO delete(Long id);

    BenchmarkDTO assignToUser(Long id, Long userId);
}
