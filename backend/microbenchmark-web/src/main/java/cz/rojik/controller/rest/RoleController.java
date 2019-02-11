package cz.rojik.controller.rest;

import cz.rojik.backend.dto.user.RoleDTO;
import cz.rojik.backend.service.RoleService;
import cz.rojik.constants.MappingURLConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for roles manipulation.
 * @author Marek Rojik (marek@rojik.cz) on 03. 02. 2019
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.ROLE)
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * Get a list of roles.
     * @return list of roles
     */
    @ApiOperation(value = "Get a list of roles which can be assigned to user", notes = "This can only be done by the logged in user with ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the list of roles"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAll() {
        List<RoleDTO> roles = roleService.getAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
