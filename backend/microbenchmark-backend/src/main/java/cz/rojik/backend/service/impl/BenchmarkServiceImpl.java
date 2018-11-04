package cz.rojik.backend.service.impl;

import cz.rojik.backend.constants.ConfigConstants;
import cz.rojik.backend.dto.MeasureMethodDTO;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.entity.MeasureMethodEntity;
import cz.rojik.backend.entity.BenchmarkEntity;
import cz.rojik.backend.entity.UserEntity;
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

@Service("benchmarkServiceBackend")
public class BenchmarkServiceImpl implements BenchmarkService {

    @Autowired
    private BenchmarkConverter benchmarkConverter;

    @Autowired
    private BenchmarkRepository benchmarkRepository;

    @Autowired
    private MeasureMethodRepository measureMethodRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public BenchmarkDTO saveResult(BenchmarkDTO result) {
        UserEntity user = userRepository.findOne(result.getUser().getId());

        if (StringUtils.isEmpty(result.getName())) {
            result.setName(result.getCreated().format(DateTimeFormatter.ofPattern(ConfigConstants.LOCAL_DATE_TIME_PATTERN)));
        }

        BenchmarkEntity entity = benchmarkConverter.dtoToEntity(result);
        entity.setUser(user);

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
}
