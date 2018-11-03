package cz.rojik.backend.service;

import cz.rojik.backend.dto.BenchmarkDTO;

public interface BenchmarkService {

    BenchmarkDTO saveResult(BenchmarkDTO result);
}
