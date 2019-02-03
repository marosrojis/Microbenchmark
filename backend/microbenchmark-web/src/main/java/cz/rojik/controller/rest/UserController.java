package cz.rojik.controller.rest;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.dto.user.UserRegistrationForm;
import cz.rojik.backend.service.UserService;
import cz.rojik.constants.MappingURLConstants;
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

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Get user by specific ID
     * @param id user id
     * @return user by specific ID
     */
    @GetMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        UserDTO user = userService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Get all existing users.
     * @param enabled optional parameter. If is true than return only users with enable account. If is false, return only users with disable account.
     * @return list of users
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam(value = "enabled") Optional<Boolean> enabled) {
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
    @PostMapping
    private ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRegistrationForm user) {
        UserDTO newUser = userService.create(user);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    /**
     * Update existing user.
     * @param id user ID to update
     * @param user new user's data to update
     * @return updated user
     */
    @PutMapping(MappingURLConstants.ID_PARAM)
    private ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO user) {
        UserDTO newUser = userService.update(id, user);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    /**
     * Delete existing user.
     * @param id user ID to delete
     */
    @DeleteMapping(MappingURLConstants.ID_PARAM)
    public ResponseEntity delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }


}
