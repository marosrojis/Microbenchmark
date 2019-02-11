package cz.rojik.controller.rest;

import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.service.PropertyService;
import cz.rojik.constants.MappingURLConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
    @ApiOperation(value = "Get a property with an ID", notes = "This can only be done by the logged in user with ADMIN role.", response = PropertyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the property"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity<PropertyDTO> getOne(@ApiParam(value = "property ID", required = true) @PathVariable Long id) {
        PropertyDTO property = propertyService.getOne(id);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    /**
     * Get all properties
     * @return list of properties
     */
    @ApiOperation(value = "Get a list of properties", notes = "This can only be done by the logged in user with ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the list of properties"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAll() {
        List<PropertyDTO> properties = propertyService.getAll();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    /**
     * Create new or update existing property.
     * If exist property with defined key, property is updated. If key is not exist, create new property
     * @param property property for create or update
     * @return updated property
     */
    @ApiOperation(value = "Create new or update existing property. If exist property with defined key, property is updated. If key is not exist, create new property.", notes = "This can only be done by the logged in user with ADMIN role.", response = PropertyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully modified the benchmark"),
            @ApiResponse(code = 400, message = "Property with specified key is not exist in system."),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PutMapping
    public ResponseEntity<PropertyDTO> update(@ApiParam(value = "property for create or update", required = true) @Valid @RequestBody PropertyDTO property) {
        property = propertyService.updateProperty(property);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    /**
     * Delete property by ID.
     * @param id property ID
     */
    @ApiOperation(value = "Delete a property by ID.", notes = "This can only be done by the logged in user with ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the property"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @DeleteMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity delete(@ApiParam(value = "property ID", required = true) @PathVariable Long id) {
        propertyService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
