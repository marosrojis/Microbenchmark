package cz.rojik.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.entity.PropertyEntity;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.repository.PropertyRepository;
import cz.rojik.backend.service.PropertyService;
import cz.rojik.backend.util.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class PropertyServiceImpl implements PropertyService {

    private static Logger LOGGER = LoggerFactory.getLogger(PropertyServiceImpl.class);

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PropertyDTO getOne(Long id) {
        LOGGER.trace("Get property by id {} from database.", id);

        Optional<PropertyEntity> entity = propertyRepository.findById(id);
        if (!entity.isPresent()) {
            throw new EntityNotFoundException(String.format("Property entity with id %s was not found.", id));
        }
        return objectMapper.convertValue(entity.get(), PropertyDTO.class);
    }

    @Override
    public PropertyDTO getByKey(String key) {
        LOGGER.trace("Get property by key {} from database.", key);

        Optional<PropertyEntity> entity = propertyRepository.findFirstByKey(key);
        if (!entity.isPresent()) {
            throw new EntityNotFoundException(String.format("Property entity with key %s was not found.", key));
        }
        return objectMapper.convertValue(entity.get(), PropertyDTO.class);
    }

    @Override
    public List<PropertyDTO> getAll() {
        LOGGER.trace("Get all properties from DB.");
        List<PropertyEntity> entities = propertyRepository.findAll();
        List<PropertyDTO> result = entities.stream().map(entity -> objectMapper.convertValue(entity, PropertyDTO.class)).collect(Collectors.toList());
        return result;
    }

    @Override
    public PropertyDTO updateProperty(PropertyDTO property) {
        LOGGER.trace("Update property in database: {}", property);
        Optional<PropertyEntity> entity = propertyRepository.findFirstByKey(property.getKey());
        PropertyEntity propertyEntity = entity.orElseGet(PropertyEntity::new);

        propertyEntity.setKey(property.getKey())
                .setValue(property.getValue());

        propertyEntity = propertyRepository.saveAndFlush(propertyEntity);
        return objectMapper.convertValue(propertyEntity, PropertyDTO.class);
    }

    @Override
    public void delete(Long id) {
        LOGGER.debug("Delete property with id {} (requested user {})", id, SecurityHelper.getCurrentUser());
        Optional<PropertyEntity> entity = propertyRepository.findById(id);
        if (!entity.isPresent()) {
            throw new EntityNotFoundException(String.format("Property with id %s was not found.", id));
        }

        propertyRepository.delete(entity.get());
        LOGGER.debug("Propery with id {} was successfully deleted", id);
    }

    @Override
    public void deleteByKey(String key) {
        LOGGER.debug("Delete property with key {} (requested user {})", key, SecurityHelper.getCurrentUser());
        Optional<PropertyEntity> entity = propertyRepository.findFirstByKey(key);
        if (!entity.isPresent()) {
            throw new EntityNotFoundException(String.format("Property with key %s was not found.", key));
        }

        propertyRepository.delete(entity.get());
        LOGGER.debug("Propery with key {} was successfully deleted", key);
    }
}
