package cz.rojik.backend.service.impl;

import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.entity.BenchmarkStateEntity;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import cz.rojik.backend.entity.UserEntity;
import cz.rojik.backend.repository.BenchmarkStateRepository;
import cz.rojik.backend.service.BenchmarkStateService;
import cz.rojik.backend.service.UserService;
import cz.rojik.backend.util.converter.BenchmarkStateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BenchmarkStateServiceImpl implements BenchmarkStateService {

    private static Logger logger = LoggerFactory.getLogger(BenchmarkStateServiceImpl.class);

    @Autowired
    private BenchmarkStateRepository benchmarkStateRepository;

    @Autowired
    private BenchmarkStateConverter benchmarkStateConverter;

    @Autowired
    private UserService userService;

    @Override
    public List<BenchmarkStateDTO> getAll() {
        List<BenchmarkStateEntity> entities = benchmarkStateRepository.findAllByOrderByUpdated();
        List<BenchmarkStateDTO> result = entities.stream().map(entity -> benchmarkStateConverter.entityToDTO(entity)).collect(Collectors.toList());

        return result;
    }

    @Override
    public List<BenchmarkStateDTO> getBenchmarksState(Optional<Boolean> running) {
        List<BenchmarkStateDTO> result;

        if (running.isPresent()) {
            if (running.get()) {
                result = getAllByState(BenchmarkStateTypeEnum.runningStates());
            }
            else {
                result = getAllByState(BenchmarkStateTypeEnum.stopStates());
            }
        }
        else {
            result = getAll();
        }
        return result;
    }

    @Override
    public List<BenchmarkStateDTO> getAllByState(List<BenchmarkStateTypeEnum> stateType) {
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

        BenchmarkStateEntity entity = benchmarkStateConverter.dtoToEntity(state);

        UserEntity loggedUser = userService.getLoggedUserEntity();
        if (loggedUser != null) {
            entity.setUser(loggedUser);
        }

        int numberOfConnections = benchmarkStateRepository.countAllByStateType(BenchmarkStateTypeEnum.runningStates());
        entity.setNumberOfConnections(++numberOfConnections);

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

        BenchmarkStateEntity entity = benchmarkStateRepository.findFirstByProjectId(state.getProjectId());
        if (entity == null) {
            throw new NotFoundException("Benchmark state was not found by project ID " + state.getProjectId());
        }

        entity.setType(state.getType());
        entity.setUpdated(state.getUpdated());
        if (state.getContainerId() != null) {
            entity.setContainerId(state.getContainerId());
        }

        entity = benchmarkStateRepository.saveAndFlush(entity);
        return benchmarkStateConverter.entityToDTO(entity);
    }

    @Transactional
    @Override
    public void increaseNumberOfConnectionsToAllActive(String projectId) {
        if (projectId == null) {
            throw new BadRequestException("The given project ID is null");
        }

        List<BenchmarkStateEntity> benchmarks = benchmarkStateRepository.findAllByProjectIdIsNotAndTypeIn(projectId, BenchmarkStateTypeEnum.runningStates());
        benchmarks.forEach(benchmark -> {
            benchmark.setNumberOfConnections(benchmark.getNumberOfConnections() + 1);
            benchmark.setUpdated(LocalDateTime.now());
        });

        benchmarks = benchmarkStateRepository.save(benchmarks);
        logger.info("Count of increase benchmarks is " + benchmarks.size());
    }
}
