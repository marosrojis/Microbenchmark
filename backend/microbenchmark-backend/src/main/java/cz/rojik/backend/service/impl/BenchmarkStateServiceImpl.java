package cz.rojik.backend.service.impl;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.entity.BenchmarkStateEntity;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import cz.rojik.backend.entity.UserEntity;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.repository.BenchmarkStateRepository;
import cz.rojik.backend.service.BenchmarkStateService;
import cz.rojik.backend.service.UserService;
import cz.rojik.backend.util.SecurityHelper;
import cz.rojik.backend.util.converter.BenchmarkStateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.ws.rs.BadRequestException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class BenchmarkStateServiceImpl implements BenchmarkStateService {

    private static Logger LOGGER = LoggerFactory.getLogger(BenchmarkStateServiceImpl.class);

    @Autowired
    private BenchmarkStateRepository benchmarkStateRepository;

    @Autowired
    private BenchmarkStateConverter benchmarkStateConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityHelper securityHelper;

    @Transactional
    @PostConstruct
    public void clearTableBenchmarkState() {
        LOGGER.trace("Clear table BenchmarkState after start application.");
        benchmarkStateRepository.deleteAll();
        LOGGER.trace("Clear table BenchmarkState was successful.");
    }

    @Override
    public BenchmarkStateDTO getByProjectId(String projectId) {
        LOGGER.trace("Get benchmark state by project ID for user {}", projectId, securityHelper.getCurrentUser());
        Optional<BenchmarkStateEntity> entity = benchmarkStateRepository.findByProjectId(projectId);
        if (!entity.isPresent()) {
            throw new EntityNotFoundException("Benchmark state was not found by project ID " + projectId);
        }

        BenchmarkStateDTO result = benchmarkStateConverter.entityToDTO(entity.get());
        return result;
    }

    @Override
    public List<BenchmarkStateDTO> getAll() {
        LOGGER.trace("Get all benchmark states for user {}", securityHelper.getCurrentUser());
        List<BenchmarkStateEntity> entities = benchmarkStateRepository.findAllByOrderByUpdated();
        List<BenchmarkStateDTO> result = entities.stream().map(entity -> benchmarkStateConverter.entityToDTO(entity)).collect(Collectors.toList());

        return result;
    }

    @Override
    public List<BenchmarkStateDTO> getBenchmarksState(Optional<Boolean> running) {
        List<BenchmarkStateDTO> result;

        synchronizeContainersWithRunningBenchmarks();

        if (running.isPresent()) {
            if (running.get()) {
                LOGGER.trace("Get all running benchmark states for user {}", securityHelper.getCurrentUser());
                result = getAllByState(BenchmarkStateTypeEnum.runningStates());
            }
            else {
                LOGGER.trace("Get all non running benchmark states for user {}", securityHelper.getCurrentUser());
                result = getAllByState(BenchmarkStateTypeEnum.stopStates());
            }
        }
        else {
            result = getAll();
        }
        LOGGER.trace("Return selected benchmark states for user {}", securityHelper.getCurrentUser());
        return result;
    }

    @Override
    public List<BenchmarkStateDTO> getAllByState(List<BenchmarkStateTypeEnum> stateType) {
        LOGGER.trace("Get all benchmark states by specific types: {}\n for user {}", stateType, securityHelper.getCurrentUser());
        List<BenchmarkStateEntity> entities = benchmarkStateRepository.findAllByTypeIsInOrderByUpdated(stateType);
        List<BenchmarkStateDTO> result = entities.stream().map(entity -> benchmarkStateConverter.entityToDTO(entity)).collect(Collectors.toList());

        return result;
    }

    @Transactional
    @Override
    public BenchmarkStateDTO createState(BenchmarkStateDTO state) {
        if (state == null) {
            throw new BadRequestException("The given benchmark state is null");
        }
        LOGGER.trace("Create benchmark state in DB: {}", state);
        BenchmarkStateEntity entity = benchmarkStateConverter.dtoToEntity(state);

        UserEntity loggedUser = userService.getLoggedUserEntity();
        if (loggedUser != null) {
            entity.setUser(loggedUser);
        }

        int numberOfConnections = benchmarkStateRepository.countAllByStateType(BenchmarkStateTypeEnum.runningStates());
        entity.setNumberOfConnections(++numberOfConnections);

        LOGGER.debug("Save benchmark state {} to DB", entity);
        entity = benchmarkStateRepository.save(entity);

        increaseNumberOfConnectionsToAllActive(entity.getProjectId());

        return benchmarkStateConverter.entityToDTO(entity);
    }

    @Transactional
    @Override
    public BenchmarkStateDTO updateState(BenchmarkStateDTO state) {
        if (state == null) {
            throw new BadRequestException("The given benchmark state is null");
        }

        Optional<BenchmarkStateEntity> entity = benchmarkStateRepository.findByProjectId(state.getProjectId());
        if (!entity.isPresent()) {
            throw new EntityNotFoundException("Benchmark state was not found by project ID " + state.getProjectId());
        }
        LOGGER.trace("Update benchmark state in DB: {}", state);

        BenchmarkStateEntity benchmarkStateEntity = entity.get();
        benchmarkStateEntity = benchmarkStateConverter.dtoToEntity(state, benchmarkStateEntity);

        benchmarkStateEntity = benchmarkStateRepository.save(benchmarkStateEntity);
        return benchmarkStateConverter.entityToDTO(benchmarkStateEntity);
    }

    @Transactional
    @Override
    public void increaseNumberOfConnectionsToAllActive(String projectId) {
        LOGGER.trace("Increate number of connections to all active benchmark state. Start compile project is {}", projectId);
        if (projectId == null) {
            throw new BadRequestException("The given project ID is null");
        }

        List<BenchmarkStateEntity> benchmarks = benchmarkStateRepository.findAllByProjectIdIsNotAndTypeInAndArchivedIsFalse(projectId, BenchmarkStateTypeEnum.runningStates());
        benchmarks.forEach(benchmark -> {
            benchmark.setNumberOfConnections(benchmark.getNumberOfConnections() + 1);
            benchmark.setUpdated(LocalDateTime.now());
        });

        benchmarkStateRepository.saveAll(benchmarks);
        LOGGER.debug("Count of increase benchmarks is " + benchmarks.size());
    }

    @Transactional
    @Override
    public void synchronizeContainersWithRunningBenchmarks() {
        LOGGER.trace("Synchronize running benchmark states with running docker containers.");
        DockerClient docker;
        List<Container> containers;
        try {
            docker = DefaultDockerClient.fromEnv().build();
            containers = docker.listContainers();
            LOGGER.debug("Running docker containers: {}", containers);
        } catch (InterruptedException | DockerException | DockerCertificateException e) {
            LOGGER.error("Docker is not running", e);
            return;
        }

        List<BenchmarkStateEntity> benchmarks = benchmarkStateRepository.findAllByTypeIsInOrderByUpdated(BenchmarkStateTypeEnum.runningBenchmarks());
        Set<String> containersId = containers.stream().map(Container::id).collect(Collectors.toSet());

        benchmarks.forEach(benchmark ->  {
            if (!containersId.contains(benchmark.getContainerId())) {
                LOGGER.info("Docker container {} for project {} is not running.", benchmark.getContainerId(), benchmark.getProjectId());
                benchmark.setType(BenchmarkStateTypeEnum.BENCHMARK_ERROR);
                benchmarkStateRepository.save(benchmark);
            }
        });
        LOGGER.trace("Synchronize running benchmark states is completed.");

    }
}
