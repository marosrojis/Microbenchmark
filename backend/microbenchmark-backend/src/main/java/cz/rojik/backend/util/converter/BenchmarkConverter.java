package cz.rojik.backend.util.converter;

import cz.rojik.backend.dto.MeasureMethodDTO;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.BenchmarkEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BenchmarkConverter {

    public BenchmarkDTO entityToDTO(BenchmarkEntity entity) {
        BenchmarkDTO result = new BenchmarkDTO();

        result.setContent(entity.getContent())
                .setCreated(entity.getCreated())
                .setDeclare(entity.getDeclare())
                .setInit(entity.getInit())
                .setMeasurement(entity.getMeasurement())
                .setWarmup(entity.getWarmup())
                .setProjectId(entity.getProjectId())
                .setUser(new UserDTO(entity.getUser()));

        List<MeasureMethodDTO> methods = new ArrayList<>();
        entity.getMeasureMethods().forEach(method -> methods.add(new MeasureMethodDTO(method)));

        result.setMeasureMethods(methods);

        return result;
    }

    public BenchmarkEntity dtoToEntity(BenchmarkDTO dto) {
        BenchmarkEntity entity = new BenchmarkEntity();

        entity.setContent(dto.getContent())
                .setCreated(dto.getCreated())
                .setDeclare(dto.getDeclare())
                .setInit(dto.getInit())
                .setMeasurement(dto.getMeasurement())
                .setWarmup(dto.getWarmup())
                .setProjectId(dto.getProjectId());

        return entity;
    }
}
