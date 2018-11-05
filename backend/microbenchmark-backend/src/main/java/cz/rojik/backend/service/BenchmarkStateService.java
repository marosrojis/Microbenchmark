package cz.rojik.backend.service;

import cz.rojik.backend.dto.BenchmarkStateDTO;

public interface BenchmarkStateService {

    BenchmarkStateDTO createState(BenchmarkStateDTO state);

    BenchmarkStateDTO updateState(BenchmarkStateDTO state);

    void increaseNumberOfConnectionsToAllActive(String projectId);
}
