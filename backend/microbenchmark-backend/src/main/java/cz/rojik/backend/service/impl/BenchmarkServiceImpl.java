package cz.rojik.backend.service.impl;

import cz.rojik.backend.constants.DateTimeConstants;
import cz.rojik.backend.dto.MeasureMethodDTO;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.entity.BenchmarkStateEntity;
import cz.rojik.backend.entity.MeasureMethodEntity;
import cz.rojik.backend.entity.BenchmarkEntity;
import cz.rojik.backend.entity.UserEntity;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.exception.UserException;
import cz.rojik.backend.repository.BenchmarkStateRepository;
import cz.rojik.backend.repository.MeasureMethodRepository;
import cz.rojik.backend.repository.BenchmarkRepository;
import cz.rojik.backend.repository.UserRepository;
import cz.rojik.backend.repository.specification.BenchmarkSpecificationBuilder;
import cz.rojik.backend.service.BenchmarkService;
import cz.rojik.backend.util.SecurityHelper;
import cz.rojik.backend.util.converter.BenchmarkConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service("benchmarkServiceBackend")
public class BenchmarkServiceImpl implements BenchmarkService {

    private static Logger LOGGER = LoggerFactory.getLogger(BenchmarkServiceImpl.class);

    @Autowired
    private BenchmarkConverter benchmarkConverter;

    @Autowired
    private BenchmarkRepository benchmarkRepository;

    @Autowired
    private MeasureMethodRepository measureMethodRepository;

    @Autowired
    private BenchmarkStateRepository benchmarkStateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityHelper securityHelper;

    @Override
    public BenchmarkDTO getOne(Long id) {
        Optional<BenchmarkEntity> benchmarkEntity = findBenchmarkById(id);
        if (!benchmarkEntity.isPresent()) {
            throw new EntityNotFoundException(String.format("Benchmark with ID %s was not found.", id));
        }

        return benchmarkConverter.entityToDTO(benchmarkEntity.get());
    }

    @Override
    public List<BenchmarkDTO> getAll(Optional<Boolean> success, Optional<Long> user) {
        if (!securityHelper.isLoggedUserAdmin() &&
                user.isPresent() && !securityHelper.hasLoggedUserId(user.get())) {
            throw new EntityNotFoundException("User with ID " + user.get() + " was not found.");
        }

        List<BenchmarkEntity> entities = benchmarkRepository.findAll(BenchmarkSpecificationBuilder.matchQuery(success, user, securityHelper));
        List<BenchmarkDTO> result = entities.stream().map(entity -> benchmarkConverter.entityToDTO(entity)).collect(Collectors.toList());

        return result;
    }

    @Override
    @Transactional
    public BenchmarkDTO saveResult(BenchmarkDTO result) {
        LOGGER.debug("Save benchmark result {}", result);

        if (StringUtils.isEmpty(result.getName())) {
            result.setName(result.getCreated().format(DateTimeFormatter.ofPattern(DateTimeConstants.LOCAL_DATE_TIME_PATTERN)));
        }

        BenchmarkEntity entity = benchmarkConverter.dtoToEntity(result);

        if (result.getUser() != null) {
            Optional<UserEntity> user = userRepository.findById(result.getUser().getId());
            entity.setUser(user.get());
        }

        entity = benchmarkRepository.saveAndFlush(entity);

        List<MeasureMethodEntity> measureMethodEntityList = new ArrayList<>();
        for (MeasureMethodDTO method : result.getMeasureMethods()) {
            MeasureMethodEntity methodEntity = new MeasureMethodEntity(method.getOrder(), method.getMethod(), entity);
            methodEntity = measureMethodRepository.save(methodEntity);
            LOGGER.trace("Save measured method {} for project {}", methodEntity, result.getProjectId());
            measureMethodEntityList.add(methodEntity);
        }

        entity.setMeasureMethods(measureMethodEntityList);
        return benchmarkConverter.entityToDTO(entity);
    }

    @Override
    public BenchmarkDTO delete(Long id) {
        LOGGER.trace("Delete benchmark {}", id);
        Optional<BenchmarkEntity> entity = findBenchmarkById(id);
        if (!entity.isPresent()) {
            throw new EntityNotFoundException(String.format("Benchmark with ID %s was not found.", id));
        }

        List<MeasureMethodEntity> methods = measureMethodRepository.findAllByBenchmark(entity.get());
        measureMethodRepository.deleteAll(methods);

        Optional<BenchmarkStateEntity> benchmarkStateEntity = benchmarkStateRepository.findByProjectId(entity.get().getProjectId());
        if (benchmarkStateEntity.isPresent()) {
            benchmarkStateRepository.delete(benchmarkStateEntity.get());
        }
        else {
            LOGGER.debug("Benchmark state with project ID {} is not exist -> it cannot be deleted.");
        }

        benchmarkRepository.delete(entity.get());
        LOGGER.debug("Deleting benchmark {} is completed.", entity);
        return benchmarkConverter.entityToDTO(entity.get());
    }

    @Override
    public BenchmarkDTO assignToUser(Long id, Long userId) {
        LOGGER.debug("Assign benchmark {} to user {}", id, userId);
        Optional<BenchmarkEntity> benchmarkEntity = benchmarkRepository.findById(id);
        if (!benchmarkEntity.isPresent()) {
            throw new EntityNotFoundException(String.format("Benchmark with ID %s was not found.", id));
        }

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (!userEntity.isPresent()) {
            throw new EntityNotFoundException(String.format("User with ID %s was not found.", userId));
        }

        BenchmarkEntity entity = benchmarkEntity.get();
        entity.setUser(userEntity.get());
        LOGGER.trace("Save benchmark {}", entity);
        benchmarkRepository.saveAndFlush(entity);

        return benchmarkConverter.entityToDTO(entity);
    }

    /**
     * Find benchmark in database based on logged user and his role.
     * If user has role 'User' od 'Demo' then add to SQL query user_id parameter.
     * @param id benchmark id
     * @return benchmark object
     */
    private Optional<BenchmarkEntity> findBenchmarkById(Long id) {
        Optional<BenchmarkEntity> benchmarkEntity;
        if (securityHelper.isLoggedUserAdmin()) {
            benchmarkEntity = benchmarkRepository.findById(id);
        }
        else {
            benchmarkEntity = benchmarkRepository.findByIdAndUserId(id, securityHelper.getCurrentUserId());
        }
        return benchmarkEntity;
    }
}
