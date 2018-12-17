package cz.rojik.backend.service;

import cz.rojik.backend.dto.PropertiesDTO;
import cz.rojik.backend.exception.EntityNotFoundException;


public interface PropertiesService {

    PropertiesDTO getProperties(String key) throws EntityNotFoundException;

    PropertiesDTO updateProperties(PropertiesDTO properties);
}
