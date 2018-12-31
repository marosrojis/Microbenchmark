package cz.rojik.controller.rest;

import cz.rojik.backend.dto.PropertiesDTO;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.service.dto.PathDTO;
import cz.rojik.service.service.ImporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.LIBRARY_LIBRARIES)
public class LibraryController {

    @Autowired
    private ImporterService importerService;

    @PostMapping
    public ResponseEntity<PropertiesDTO> updateJavaLibraries(@Valid @RequestBody PathDTO path) {
        PropertiesDTO result = importerService.processFolderWithJars(path.getPath());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // TODO: GetMapping, DeleteMapping
}
