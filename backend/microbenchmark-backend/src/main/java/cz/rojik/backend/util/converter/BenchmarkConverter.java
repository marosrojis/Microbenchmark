package cz.rojik.backend.util.converter;

import cz.rojik.backend.dto.MeasureMethodDTO;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.BenchmarkEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BenchmarkConverter {

    private static Logger logger = LoggerFactory.getLogger(BenchmarkConverter.class);

    @Autowired
    private UserConverter userConverter;

    public BenchmarkDTO entityToDTO(BenchmarkEntity entity) {
        logger.trace("Convert Benchmark entity to DTO object: {}", entity);
        BenchmarkDTO result = new BenchmarkDTO();

        result.setContent(entity.getContent())
                .setName(entity.getName())
                .setCreated(entity.getCreated())
                .setDeclare(entity.getDeclare())
                .setInit(entity.getInit())
                .setMeasurement(entity.getMeasurement())
                .setWarmup(entity.getWarmup())
                .setProjectId(entity.getProjectId())
                .setUser(userConverter.entityToDTO(entity.getUser(), false));
        result.setId(entity.getId());

        List<MeasureMethodDTO> methods = new ArrayList<>();
        entity.getMeasureMethods().forEach(method -> methods.add(new MeasureMethodDTO(method)));

        result.setMeasureMethods(methods);

        logger.trace("Converted Benchmark measure methods DTO: {}", methods);

        return result;
    }

    public BenchmarkEntity dtoToEntity(BenchmarkDTO dto) {
        logger.trace("Convert DTO to Entity: {}", dto);
        BenchmarkEntity entity = new BenchmarkEntity();

        entity.setContent(dto.getContent())
                .setName(dto.getName())
                .setCreated(dto.getCreated())
                .setDeclare(dto.getDeclare())
                .setInit(dto.getInit())
                .setMeasurement(dto.getMeasurement())
                .setWarmup(dto.getWarmup())
                .setProjectId(dto.getProjectId());

        return entity;
    }
}
