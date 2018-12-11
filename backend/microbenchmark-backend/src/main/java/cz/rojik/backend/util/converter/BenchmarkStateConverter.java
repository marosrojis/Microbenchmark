package cz.rojik.backend.util.converter;

import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.BenchmarkStateEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BenchmarkStateConverter {

    private static Logger logger = LoggerFactory.getLogger(BenchmarkConverter.class);

    @Autowired
    private UserConverter userConverter;

    public BenchmarkStateDTO entityToDTO(BenchmarkStateEntity entity) {
        logger.trace("Convert BenchmarkState entity to DTO object: {}", entity);

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
        logger.trace("Convert BenchmarkState entity to DTO object: {}", dto);

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
