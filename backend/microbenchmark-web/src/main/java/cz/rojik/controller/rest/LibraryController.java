package cz.rojik.controller.rest;

import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.service.PropertyService;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.backend.constants.PropertyConstants;
import cz.rojik.service.dto.PathDTO;
import cz.rojik.service.service.ImporterService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller for java library manipulation.
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.LIBRARY_LIBRARIES)
public class LibraryController {

    @Autowired
    private ImporterService importerService;

    @Autowired
    private PropertyService propertyService;

    /**
     * Get map of java libraries from database.
     * This map of java libraries is used during searching imports for found java classes.
     * @return map of java library.
     */
    @ApiOperation(value = "Get map of java libraries from database. This map of java libraries is used during searching imports for found java classes.",
            notes = "This can only be done by the logged in user with ADMIN role.", response = PropertyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the property"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<PropertyDTO> get() {
        PropertyDTO property = propertyService.getByKey(PropertyConstants.LIBRARIES_CACHE);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    /**
     * Update map of java libraries.
     * @param path specify path with folder with JAR files to analyze.
     * @return created new map of java libraries
     */
    @ApiOperation(value = "Update map of java libraries.", notes = "This can only be done by the logged in user with ADMIN role.", response = PropertyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the map of java libraries"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PutMapping
    public ResponseEntity<PropertyDTO> updateJavaLibraries(@ApiParam(value = "path to directory with JAR files", required = true) @Valid @RequestBody PathDTO path) {
        PropertyDTO result = importerService.processFolderWithJars(path.getPath());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete map of java libraries.
     */
    @ApiOperation(value = "Delete map of java libraries.", notes = "This can only be done by the logged in user with ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the map of java libraries"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @DeleteMapping
    public ResponseEntity delete() {
        propertyService.deleteByKey(PropertyConstants.LIBRARIES_CACHE);
        return new ResponseEntity(HttpStatus.OK);
    }
}
