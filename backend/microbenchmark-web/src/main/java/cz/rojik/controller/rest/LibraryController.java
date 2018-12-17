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

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.LIBRARY_LIBRARIES)
public class LibraryController {

    @Autowired
    private ImporterService importerService;

    @PostMapping
    public ResponseEntity<PropertiesDTO> updateJavaLibraries(@RequestBody PathDTO path) {
        PropertiesDTO result = importerService.processFolderWithJar(path.getPath());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
