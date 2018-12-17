package cz.rojik.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rojik.backend.dto.PropertiesDTO;
import cz.rojik.backend.entity.PropertiesEntity;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.repository.PropertiesRepository;
import cz.rojik.backend.service.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PropertiesServiceImpl implements PropertiesService {

    private static Logger logger = LoggerFactory.getLogger(PropertiesServiceImpl.class);

    @Autowired
    private PropertiesRepository propertiesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PropertiesDTO getProperties(String key) {
        logger.trace("Get properties by key {} from database.", key);

        PropertiesEntity entity = propertiesRepository.getFirstByKey(key);
        if (entity == null) {
            throw new EntityNotFoundException("Properties entity with key " + key + "was not found.");
        }
        return objectMapper.convertValue(entity, PropertiesDTO.class);
    }

    @Override
    public PropertiesDTO updateProperties(PropertiesDTO properties) {
        logger.trace("Update properties in database: {}", properties);
        PropertiesEntity entity = propertiesRepository.getFirstByKey(properties.getKey());

        if (entity == null) {
            entity = new PropertiesEntity();
        }
        entity.setKey(properties.getKey())
                .setValue(properties.getValue());

        entity = propertiesRepository.saveAndFlush(entity);
        return objectMapper.convertValue(entity, PropertiesDTO.class);
    }


}
