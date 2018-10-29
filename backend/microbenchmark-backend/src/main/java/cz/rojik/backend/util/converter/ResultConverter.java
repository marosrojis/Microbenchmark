package cz.rojik.backend.util.converter;

import cz.rojik.backend.dto.MeasureMethodDTO;
import cz.rojik.backend.dto.ResultDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResultConverter {

    public ResultDTO entityToDTO(ResultEntity entity) {
        ResultDTO result = new ResultDTO();

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

    public ResultEntity dtoToEntity(ResultDTO dto) {
        ResultEntity entity = new ResultEntity();

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
