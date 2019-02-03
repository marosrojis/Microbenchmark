package cz.rojik.controller;

import cz.rojik.MBMarkApplicationTest;
import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.repository.PropertyRepository;
import cz.rojik.controller.rest.LibraryController;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static cz.rojik.mock.MockConst.*;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 03. 02. 2019
 */
public class LibraryControllerTest extends MBMarkApplicationTest {

    @Autowired
    private LibraryController libraryController;

    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    public void getTest() {
        ResponseEntity<PropertyDTO> response = libraryController.get();
        PropertyDTO property = response.getBody();

        Assert.assertEquals(property.getId(), PROPERTY_ID_4);
        Assert.assertEquals(property.getKey(), PROPERTY_KEY_4);
        Assert.assertEquals(property.getValue(), PROPERTY_VALUE_4);
    }

    @Test
    public void deleteTest() {
        libraryController.delete();

        Assert.assertEquals(propertyRepository.count(), 3);
        Assert.assertTrue(propertyRepository.findAll().stream().noneMatch(s -> s.getKey().equalsIgnoreCase(PROPERTY_KEY_4)));
    }
}
