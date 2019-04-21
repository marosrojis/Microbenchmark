package cz.rojik.controller.rest;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.dto.user.UserRegistrationForm;
import cz.rojik.backend.service.UserService;
import cz.rojik.constants.MappingURLConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller for java user manipulation.
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(MappingURLConstants.USERS)
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get user by specific ID
     * @param id user id
     * @return user by specific ID
     */
    @ApiOperation(value = "Get a user with an ID", notes = "This can only be done by the logged in user.", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the user"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity<UserDTO> getUser(@ApiParam(value = "user ID", required = true) @PathVariable Long id) {
        UserDTO user = userService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Get all existing users.
     * @param enabled optional parameter. If is true than return only users with enable account. If is false, return only users with disable account.
     * @return list of users
     */
    @ApiOperation(value = "Get a list of existing users.", notes = "This can only be done by the logged in user with ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the list of users"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(@ApiParam(value = "optional parameter. If is true than return only users with enable account. If is false, return only users with disable account.")
                                                      @RequestParam(value = "enabled") Optional<Boolean> enabled) {
        List<UserDTO> users = userService.getAll(enabled);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Create new user.
     * If user is created by anonymous user, account has to be confirm by ADMIN.
     * If user is created by user with 'ADMIN' role, account is automatically confirmed.
     * @param user user to create
     * @return created user
     */
    @ApiOperation(value = "Create new user. If user is created by anonymous user, account has to be confirm by ADMIN. If user is created by user with 'ADMIN' role, account is automatically confirmed.", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created the user"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@ApiParam(value = "user to create", required = true) @Valid @RequestBody UserRegistrationForm user) {
        UserDTO newUser = userService.create(user);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    /**
     * Update existing user.
     * @param id user ID to update
     * @param user new user's data to update
     * @return updated user
     */
    @ApiOperation(value = "Update existing user", notes = "This can only be done by the logged in user.", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully modified the user"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PutMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity<UserDTO> updateUser(@ApiParam(value = "user ID to update", required = true) @PathVariable Long id,
                                              @ApiParam(value = "New user's data to update", required = true) @Valid @RequestBody UserDTO user) {
        UserDTO newUser = userService.update(id, user);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    /**
     * Delete existing user.
     * @param id user ID to delete
     */
    @ApiOperation(value = "Delete specific user", notes = "This can only be done by the logged in user with ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the user"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @DeleteMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity delete(@ApiParam(value = "user ID to delete", required = true) @PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }


}
