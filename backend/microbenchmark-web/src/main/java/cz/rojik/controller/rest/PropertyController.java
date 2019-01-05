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

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.PROPERTY)
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity<PropertyDTO> getOne(@PathVariable Long id) {
        PropertyDTO property = propertyService.getOne(id);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAll() {
        List<PropertyDTO> properties = propertyService.getAll();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PropertyDTO> update(@Valid @RequestBody PropertyDTO property) {
        property = propertyService.updateProperty(property);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    @DeleteMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity delete(@PathVariable Long id) {
        propertyService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
