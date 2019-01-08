package cz.rojik.backend.service;

import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.exception.EntityNotFoundException;

import java.util.List;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface PropertyService {

    /**
     * Get property based on ID
     * @param id property ID
     * @return property object
     */
    PropertyDTO getOne(Long id);

    /***
     * Get property based on key
     * @param key property key
     * @return property object
     */
    PropertyDTO getByKey(String key);

    /**
     * Get all properties from database
     * @return list of properties
     */
    List<PropertyDTO> getAll();

    /**
     * Update property in database
     * @param property property with new values
     * @return updated property
     */
    PropertyDTO updateProperty(PropertyDTO property);

    /**
     * Delete property based on ID
     * @param id property ID
     */
    void delete(Long id);

    /**
     * Delete property based on key
     * @param key property key
     */
    void deleteByKey(String key);
}
