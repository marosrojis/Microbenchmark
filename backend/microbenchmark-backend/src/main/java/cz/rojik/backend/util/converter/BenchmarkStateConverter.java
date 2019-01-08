package cz.rojik.backend.util.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.BenchmarkStateEntity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Component
public class BenchmarkStateConverter {

    private static Logger logger = LoggerFactory.getLogger(BenchmarkConverter.class);

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Convert {@link BenchmarkStateEntity} to {@link BenchmarkStateDTO} object
     * @param entity benchmark state entity object
     * @return {@link BenchmarkStateDTO} object
     */
    public BenchmarkStateDTO entityToDTO(BenchmarkStateEntity entity) {
        logger.trace("Convert BenchmarkState entity to DTO object: {}", entity);

        BenchmarkStateDTO result = new BenchmarkStateDTO();

        result.setId(entity.getId());
        result.setContainerId(entity.getContainerId())
                .setNumberOfConnections(entity.getNumberOfConnections())
                .setProjectId(entity.getProjectId())
                .setType(entity.getType())
                .setUpdated(entity.getUpdated())
                .setCompleted(entity.getCompleted())
                .setTimeToEnd(entity.getTimeToEnd());
        if (entity.getUser() != null) {
            result.setUser(userConverter.entityToDTO(entity.getUser(), false));
        }

        return result;
    }

    /**
     * Convert {@link BenchmarkStateDTO} to {@link BenchmarkStateEntity} object
     * @param dto benchmark state dto object to convert
     * @return {@link BenchmarkStateEntity} object
     */
    public BenchmarkStateEntity dtoToEntity(BenchmarkStateDTO dto) {
        logger.trace("Convert BenchmarkState DTO to entity object: {}", dto);

        BenchmarkStateEntity entity = objectMapper.convertValue(dto, BenchmarkStateEntity.class);
        return entity;
    }

    /**
     * Convert {@link BenchmarkStateDTO} to {@link BenchmarkStateEntity} object
     * @param dto benchmark state dto object to convert
     * @return {@link BenchmarkStateEntity} object
     */
    public BenchmarkStateEntity dtoToEntity(BenchmarkStateDTO dto, BenchmarkStateEntity entity) {
        logger.trace("Convert BenchmarkState DTO to entity object: {}", dto);

        if (!StringUtils.equals(dto.getContainerId(), entity.getContainerId())) {
            entity.setContainerId(dto.getContainerId());
        }
        if (!StringUtils.equals(dto.getProjectId(), entity.getProjectId())) {
            entity.setProjectId(dto.getProjectId());
        }

        entity.setType(dto.getType())
                .setUpdated(dto.getUpdated())
                .setTimeToEnd(dto.getTimeToEnd())
                .setCompleted(dto.getCompleted());

        return entity;
    }
}
