package cz.rojik.backend.service;

import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.entity.BenchmarkStateEntity;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;

import java.util.List;
import java.util.Optional;

public interface BenchmarkStateService {

    List<BenchmarkStateDTO> getAll();

    List<BenchmarkStateDTO> getBenchmarksState(Optional<Boolean> running);

    List<BenchmarkStateDTO> getAllByState(List<BenchmarkStateTypeEnum> stateType);

    BenchmarkStateDTO createState(BenchmarkStateDTO state);

    BenchmarkStateDTO updateState(BenchmarkStateDTO state);

    void increaseNumberOfConnectionsToAllActive(String projectId);

    void synchronizeContainersWithRunningBenchmarks();
}
