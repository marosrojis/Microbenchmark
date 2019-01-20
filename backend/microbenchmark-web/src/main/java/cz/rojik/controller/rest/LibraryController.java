package cz.rojik.controller.rest;

import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.service.PropertyService;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.service.constants.OtherConstants;
import cz.rojik.service.dto.PathDTO;
import cz.rojik.service.service.ImporterService;
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
    @GetMapping
    public ResponseEntity<PropertyDTO> get() {
        PropertyDTO property = propertyService.getByKey(OtherConstants.LIBRARIES_CACHE);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    /**
     * Update map of java libraries.
     * @param path specify path with folder with JAR files to analyze.
     * @return created new map of java libraries
     */
    @PutMapping
    public ResponseEntity<PropertyDTO> updateJavaLibraries(@Valid @RequestBody PathDTO path) {
        PropertyDTO result = importerService.processFolderWithJars(path.getPath());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete map of java libraries.
     */
    @DeleteMapping
    public ResponseEntity delete() {
        propertyService.deleteByKey(OtherConstants.LIBRARIES_CACHE);
        return new ResponseEntity(HttpStatus.OK);
    }
}
