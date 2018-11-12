package cz.rojik.backend.service.impl;

import cz.rojik.backend.constants.ConfigConstants;
import cz.rojik.backend.dto.MeasureMethodDTO;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.entity.BenchmarkStateEntity;
import cz.rojik.backend.entity.MeasureMethodEntity;
import cz.rojik.backend.entity.BenchmarkEntity;
import cz.rojik.backend.entity.UserEntity;
import cz.rojik.backend.exception.BenchmarkNotFoundException;
import cz.rojik.backend.exception.UserException;
import cz.rojik.backend.repository.BenchmarkStateRepository;
import cz.rojik.backend.repository.MeasureMethodRepository;
import cz.rojik.backend.repository.BenchmarkRepository;
import cz.rojik.backend.repository.UserRepository;
import cz.rojik.backend.service.BenchmarkService;
import cz.rojik.backend.util.converter.BenchmarkConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("benchmarkServiceBackend")
public class BenchmarkServiceImpl implements BenchmarkService {

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
        BenchmarkEntity benchmarkEntity = benchmarkRepository.findOne(id);
        if (benchmarkEntity == null) {
            throw new BenchmarkNotFoundException(String.format("Benchmark with ID %s was not found.", id));
        }

        return benchmarkConverter.entityToDTO(benchmarkEntity);
    }

    @Override
    public List<BenchmarkDTO> getAll() {
        List<BenchmarkEntity> entities = benchmarkRepository.findAllOrOrderByCreated();
        List<BenchmarkDTO> result = entities.stream().map(entity -> benchmarkConverter.entityToDTO(entity)).collect(Collectors.toList());

        return result;
    }

    @Override
    @Transactional
    public BenchmarkDTO saveResult(BenchmarkDTO result) {

        if (StringUtils.isEmpty(result.getName())) {
            result.setName(result.getCreated().format(DateTimeFormatter.ofPattern(ConfigConstants.LOCAL_DATE_TIME_PATTERN)));
        }

        BenchmarkEntity entity = benchmarkConverter.dtoToEntity(result);

        if (result.getUser() != null) {
            UserEntity user = userRepository.findOne(result.getUser().getId());
            entity.setUser(user);
        }

        entity = benchmarkRepository.saveAndFlush(entity);

        List<MeasureMethodEntity> measureMethodEntityList = new ArrayList<>();
        for (MeasureMethodDTO method : result.getMeasureMethods()) {
            MeasureMethodEntity methodEntity = new MeasureMethodEntity()
                    .setMethod(method.getMethod())
                    .setOrder(method.getOrder())
                    .setResult(entity);
            methodEntity = measureMethodRepository.save(methodEntity);
            measureMethodEntityList.add(methodEntity);
        }

        entity.setMeasureMethods(measureMethodEntityList);
        return benchmarkConverter.entityToDTO(entity);
    }

    @Override
    public BenchmarkDTO delete(Long id) {
        BenchmarkEntity entity = benchmarkRepository.findOne(id);
        if (entity == null) {
            throw new BenchmarkNotFoundException(String.format("Benchmark with ID %s was not found.", id));
        }

        List<MeasureMethodEntity> methods = measureMethodRepository.findAllByResult(entity);
        measureMethodRepository.delete(methods);

        BenchmarkStateEntity benchmarkStateEntity = benchmarkStateRepository.findFirstByProjectId(entity.getProjectId());
        benchmarkStateRepository.delete(benchmarkStateEntity);

        benchmarkRepository.delete(entity);
        return benchmarkConverter.entityToDTO(entity);
    }

    @Override
    public BenchmarkDTO assignToUser(Long id, Long userId) {
        BenchmarkEntity benchmarkEntity = benchmarkRepository.findOne(id);
        if (benchmarkEntity == null) {
            throw new BenchmarkNotFoundException(String.format("Benchmark with ID %s was not found.", id));
        }

        UserEntity userEntity = userRepository.findOne(userId);
        if (userEntity == null) {
            throw new UserException(String.format("User with ID %s was not found.", userId));
        }

        benchmarkEntity.setUser(userEntity);
        benchmarkRepository.save(benchmarkEntity);

        return benchmarkConverter.entityToDTO(benchmarkEntity);
    }
}
