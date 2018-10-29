package cz.rojik.backend.service.impl;

import cz.rojik.backend.dto.MeasureMethodDTO;
import cz.rojik.backend.dto.ResultDTO;
import cz.rojik.backend.entity.MeasureMethodEntity;
import cz.rojik.backend.entity.ResultEntity;
import cz.rojik.backend.entity.UserEntity;
import cz.rojik.backend.repository.MeasureMethodRepository;
import cz.rojik.backend.repository.ResultRepository;
import cz.rojik.backend.repository.UserRepository;
import cz.rojik.backend.service.ResultService;
import cz.rojik.backend.util.converter.ResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultConverter resultConverter;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private MeasureMethodRepository measureMethodRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public ResultDTO saveResult(ResultDTO result) {
        UserEntity user = userRepository.findOne(result.getUser().getId());

        ResultEntity entity = resultConverter.dtoToEntity(result);
        entity.setUser(user);

        entity = resultRepository.saveAndFlush(entity);

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
        return resultConverter.entityToDTO(entity);
    }
}
