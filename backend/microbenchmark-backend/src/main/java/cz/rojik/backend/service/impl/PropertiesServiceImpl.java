package cz.rojik.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rojik.backend.dto.PropertiesDTO;
import cz.rojik.backend.entity.PropertiesEntity;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.repository.PropertiesRepository;
import cz.rojik.backend.service.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PropertiesServiceImpl implements PropertiesService {

    @Autowired
    private PropertiesRepository propertiesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PropertiesDTO getProperties(String key) {
        PropertiesEntity entity = propertiesRepository.getFirstByKey(key);
        if (entity == null) {
            throw new EntityNotFoundException("Properties entity with key " + key + "was not found.");
        }
        return objectMapper.convertValue(entity, PropertiesDTO.class);
    }

    @Override
    public PropertiesDTO updateProperties(PropertiesDTO properties) {
        PropertiesEntity entity = propertiesRepository.getFirstByKey(properties.getKey());
        if (entity == null) {
            entity = new PropertiesEntity();
        }
        entity.setKey(properties.getKey())
                .setValue(properties.getValue());

        return objectMapper.convertValue(entity, PropertiesDTO.class);
    }


}
