package cz.rojik.backend.util.converter;

import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.BenchmarkStateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BenchmarkStateConverter {

    @Autowired
    private UserConverter userConverter;

    public BenchmarkStateDTO entityToDTO(BenchmarkStateEntity entity) {
        BenchmarkStateDTO result = new BenchmarkStateDTO();

        result.setId(entity.getId());
        result.setContainerId(entity.getContainerId())
                .setNumberOfConnections(entity.getNumberOfConnections())
                .setProjectId(entity.getProjectId())
                .setType(entity.getType())
                .setUpdated(entity.getUpdated())
                .setUser(userConverter.entityToDTO(entity.getUser(), false));

        return result;
    }

    public BenchmarkStateEntity dtoToEntity(BenchmarkStateDTO dto) {
        BenchmarkStateEntity entity = new BenchmarkStateEntity();

        entity.setId(dto.getId());
        entity.setContainerId(dto.getContainerId())
                .setNumberOfConnections(dto.getNumberOfConnections())
                .setProjectId(dto.getProjectId())
                .setType(dto.getType())
                .setUpdated(dto.getUpdated());

        return entity;
    }
}
