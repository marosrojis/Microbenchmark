package cz.rojik.controller.rest;

import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.service.PropertyService;
import cz.rojik.constants.MappingURLConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller for java property manipulation.
 * Property are used for specific some server information (e. g. java version to compile JMH project, map of java libraries, version of JMH, ...)
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.PROPERTY)
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    /**
     * Get property by ID
     * @param id property ID
     * @return property by specific ID
     */
    @GetMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity<PropertyDTO> getOne(@PathVariable Long id) {
        PropertyDTO property = propertyService.getOne(id);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    /**
     * Get all properties
     * @return list of properties
     */
    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAll() {
        List<PropertyDTO> properties = propertyService.getAll();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    /**
     * Create new or update existing property.
     * If exist property with defined key, property is updated. If key is not exist, create new property
     * @param property property for create or update
     * @return
     */
    @PostMapping
    public ResponseEntity<PropertyDTO> update(@Valid @RequestBody PropertyDTO property) {
        property = propertyService.updateProperty(property);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    /**
     * Delete property by ID.
     * @param id property ID
     */
    @DeleteMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity delete(@PathVariable Long id) {
        propertyService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
