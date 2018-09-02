package cz.rojik.controller;

import cz.rojik.constants.MappingURLConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @RequestMapping(MappingURLConstants.MAIN_PAGE)
    public ResponseEntity<?> homepage() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
