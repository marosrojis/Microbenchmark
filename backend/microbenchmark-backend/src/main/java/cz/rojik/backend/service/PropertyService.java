package cz.rojik.backend.service;

import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.exception.EntityNotFoundException;

import java.util.List;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface PropertyService {

    PropertyDTO getOne(Long id);

    PropertyDTO getByKey(String key) throws EntityNotFoundException;

    List<PropertyDTO> getAll();

    PropertyDTO updateProperty(PropertyDTO property);

    void delete(Long id);

    void deleteByKey(String key);
}
