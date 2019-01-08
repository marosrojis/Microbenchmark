package cz.rojik.backend.service;

import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.entity.BenchmarkStateEntity;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;

import java.util.List;
import java.util.Optional;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface BenchmarkStateService {

    /**
     * Get benchmark state based on project ID
     * @param projectId project ID of benchmark state
     * @return benchmark state object
     */
    BenchmarkStateDTO getByProjectId(String projectId);

    /**
     * Get all benchmark states from database
     * @return list of benchmark states
     */
    List<BenchmarkStateDTO> getAll();

    /**
     * Get all benchmark states from database based on optional parameter. If parameter is not present, get all benchmark states.
     * @param running Optional parameter to get only running/not running states.
     * @return list of benchmark states
     */
    List<BenchmarkStateDTO> getBenchmarksState(Optional<Boolean> running);

    /**
     * Get all benchmark states based on list of type of benchmark states.
     * @param stateType type of benchmark states
     * @return list of benchmark states
     */
    List<BenchmarkStateDTO> getAllByState(List<BenchmarkStateTypeEnum> stateType);

    /**
     * Create new benchmark state.
     * @param state new benchmark state
     * @return created benchmark state
     */
    BenchmarkStateDTO createState(BenchmarkStateDTO state);

    /**
     * Update existing benchmark state based on project ID.
     * @param state benchmark state to update
     * @return updated benchmark state
     */
    BenchmarkStateDTO updateState(BenchmarkStateDTO state);

    /**
     * Method for increase number of connections to all running benchmark states.
     * @param projectId project ID of benchmark state which started and affected other running benchmarks.
     */
    void increaseNumberOfConnectionsToAllActive(String projectId);

    /**
     * Method to synchronize running benchmark states from database with running docker containers.
     */
    void synchronizeContainersWithRunningBenchmarks();
}
