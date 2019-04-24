package cz.rojik.controller;

import cz.rojik.MBMarkApplicationTest;
import cz.rojik.backend.constants.PropertyConstants;
import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.repository.PropertyRepository;
import cz.rojik.controller.rest.PropertyController;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static cz.rojik.mock.MockConst.*;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 03. 02. 2019
 */
public class PropertyControllerTest extends MBMarkApplicationTest {

    @Autowired
    private PropertyController propertyController;

    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    public void getOneTest() {
        ResponseEntity<PropertyDTO> response = propertyController.getOne(PROPERTY_ID_1);
        PropertyDTO property = response.getBody();

        Assert.assertEquals(property.getId(), PROPERTY_ID_1);
        Assert.assertEquals(property.getKey(), PROPERTY_KEY_1);
        Assert.assertEquals(property.getValue(), PROPERTY_VALUE_1);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getOneExceptionTest() {
        propertyController.getOne(UNKNOWN_ID);
    }

    @Test
    public void getAllTest() {
        ResponseEntity<List<PropertyDTO>> response = propertyController.getAll();
        List<PropertyDTO> properties = response.getBody();

        Assert.assertEquals(properties.size(), propertyRepository.count() + 2);
        Assert.assertTrue(properties.stream().allMatch(p -> p.getKey().equalsIgnoreCase(PROPERTY_KEY_1) ||
            p.getKey().equalsIgnoreCase(PROPERTY_KEY_2) ||
            p.getKey().equalsIgnoreCase(PROPERTY_KEY_3) ||
            p.getKey().equalsIgnoreCase(PROPERTY_KEY_4) ||
            p.getKey().equalsIgnoreCase(PropertyConstants.JMH_VERSION) ||
            p.getKey().equalsIgnoreCase(PropertyConstants.MAX_MEMORY)));
    }

    @Test
    public void updateTest() {
        PropertyDTO property = new PropertyDTO().setKey(PROPERTY_KEY_1).setValue("new-property-value");
        ResponseEntity<PropertyDTO> response = propertyController.update(property);
        PropertyDTO updatedProperty = response.getBody();

        Assert.assertEquals(propertyRepository.count(), 4);
        Assert.assertEquals(property.getKey(), updatedProperty.getKey());
        Assert.assertEquals(property.getValue(), updatedProperty.getValue());
    }

    @Test
    public void updateNewTest() {
        PropertyDTO property = new PropertyDTO().setKey(PropertyConstants.JMH_VERSION).setValue("new-property-value");
        ResponseEntity<PropertyDTO> response = propertyController.update(property);
        PropertyDTO updatedProperty = response.getBody();

        Assert.assertEquals(propertyRepository.count(), 5);
        Assert.assertEquals(property.getKey(), updatedProperty.getKey());
        Assert.assertEquals(property.getValue(), updatedProperty.getValue());
    }

    @Test
    public void deleteTest() {
        propertyController.delete(PROPERTY_ID_1);

        Assert.assertEquals(propertyRepository.count(), 3);
        Assert.assertTrue(propertyRepository.findAll().stream().noneMatch(p -> p.getKey().equalsIgnoreCase(PROPERTY_KEY_1)));
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteExceptionTest() {
        propertyController.delete(UNKNOWN_ID);
    }
}
