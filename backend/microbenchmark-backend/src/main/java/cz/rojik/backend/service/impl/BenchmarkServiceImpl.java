package cz.rojik.backend.service.impl;

import cz.rojik.backend.constants.ConfigConstants;
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

@Service("benchmarkServiceBackend")
public class BenchmarkServiceImpl implements BenchmarkService {

    private static Logger logger = LoggerFactory.getLogger(BenchmarkServiceImpl.class);

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

    @Override
    public BenchmarkDTO getOne(Long id) {
        Optional<BenchmarkEntity> benchmarkEntity = benchmarkRepository.findById(id);
        if (!benchmarkEntity.isPresent()) {
            throw new EntityNotFoundException(String.format("Benchmark with ID %s was not found.", id));
        }

        return benchmarkConverter.entityToDTO(benchmarkEntity.get());
    }

    @Override
    public List<BenchmarkDTO> getAll(Optional<Boolean> success) {
        List<BenchmarkEntity> entities;
        if (success.isPresent()) {
            if (success.get()) {
                logger.trace("Get all successful benchmark from DB for user {}", SecurityHelper.getCurrentUser());
                entities = benchmarkRepository.findAllSuccessOrderByCreated(true);
            }
            else {
                logger.trace("Get all error benchmark from DB for user {}", SecurityHelper.getCurrentUser());
                entities = benchmarkRepository.findAllSuccessOrderByCreated(false);
            }
        }
        else {
            logger.trace("Get all benchmark from DB for user {}", SecurityHelper.getCurrentUser());
            entities = benchmarkRepository.findAllOrderByCreated();
        }
        List<BenchmarkDTO> result = entities.stream().map(entity -> benchmarkConverter.entityToDTO(entity)).collect(Collectors.toList());

        return result;
    }

    @Override
    @Transactional
    public BenchmarkDTO saveResult(BenchmarkDTO result) {
        logger.debug("Save benchmark result {}", result);

        if (StringUtils.isEmpty(result.getName())) {
            result.setName(result.getCreated().format(DateTimeFormatter.ofPattern(ConfigConstants.LOCAL_DATE_TIME_PATTERN)));
        }

        BenchmarkEntity entity = benchmarkConverter.dtoToEntity(result);

        if (result.getUser() != null) {
            Optional<UserEntity> user = userRepository.findById(result.getUser().getId());
            entity.setUser(user.get());
        }

        entity = benchmarkRepository.saveAndFlush(entity);

        List<MeasureMethodEntity> measureMethodEntityList = new ArrayList<>();
        for (MeasureMethodDTO method : result.getMeasureMethods()) {
            MeasureMethodEntity methodEntity = new MeasureMethodEntity()
                    .setMethod(method.getMethod())
                    .setOrder(method.getOrder())
                    .setResult(entity);
            methodEntity = measureMethodRepository.save(methodEntity);
            logger.trace("Save measured method {} for project {}", methodEntity, result.getProjectId());
            measureMethodEntityList.add(methodEntity);
        }

        entity.setMeasureMethods(measureMethodEntityList);
        return benchmarkConverter.entityToDTO(entity);
    }

    @Override
    public BenchmarkDTO delete(Long id) {
        logger.trace("Delete benchmark {}", id);
        Optional<BenchmarkEntity> entity = benchmarkRepository.findById(id);
        if (!entity.isPresent()) {
            throw new EntityNotFoundException(String.format("Benchmark with ID %s was not found.", id));
        }

        List<MeasureMethodEntity> methods = measureMethodRepository.findAllByResult(entity.get());
        measureMethodRepository.deleteAll(methods);

        BenchmarkStateEntity benchmarkStateEntity = benchmarkStateRepository.findFirstByProjectIdAndArchivedIsFalse(entity.get().getProjectId());
        benchmarkStateRepository.delete(benchmarkStateEntity);

        benchmarkRepository.delete(entity.get());
        logger.debug("Deleting benchmark {} is completed.", entity);
        return benchmarkConverter.entityToDTO(entity.get());
    }

    @Override
    public BenchmarkDTO assignToUser(Long id, Long userId) {
        logger.debug("Assign benchmark {} to user {}", id, userId);
        Optional<BenchmarkEntity> benchmarkEntity = benchmarkRepository.findById(id);
        if (!benchmarkEntity.isPresent()) {
            throw new EntityNotFoundException(String.format("Benchmark with ID %s was not found.", id));
        }

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (!userEntity.isPresent()) {
            throw new UserException(String.format("User with ID %s was not found.", userId));
        }

        BenchmarkEntity entity = benchmarkEntity.get();
        entity.setUser(userEntity.get());
        logger.trace("Save benchmark {}", entity);
        benchmarkRepository.saveAndFlush(entity);

        return benchmarkConverter.entityToDTO(entity);
    }
}
