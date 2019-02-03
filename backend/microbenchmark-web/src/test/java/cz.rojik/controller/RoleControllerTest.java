package cz.rojik.controller;

import cz.rojik.MBMarkApplicationTest;
import cz.rojik.backend.dto.user.RoleDTO;
import cz.rojik.backend.repository.RoleRepository;
import cz.rojik.controller.rest.RoleController;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static cz.rojik.mock.MockConst.*;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 03. 02. 2019
 */
public class RoleControllerTest extends MBMarkApplicationTest {

    @Autowired
    private RoleController roleController;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void getAllTest() {
        ResponseEntity<List<RoleDTO>> response = roleController.getAll();
        List<RoleDTO> roles = response.getBody();

        Assert.assertEquals(roles.size(), roleRepository.count());
        Assert.assertTrue(roles.stream().allMatch(r -> r.getType().equalsIgnoreCase(ROLE_TYPE_1.getRoleType()) ||
                r.getType().equalsIgnoreCase(ROLE_TYPE_2.getRoleType()) ||
                r.getType().equalsIgnoreCase(ROLE_TYPE_3.getRoleType())));
    }
}
