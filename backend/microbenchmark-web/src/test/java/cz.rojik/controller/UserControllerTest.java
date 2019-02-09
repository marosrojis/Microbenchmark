package cz.rojik.controller;

import cz.rojik.MBMarkApplicationTest;
import cz.rojik.backend.dto.user.RoleDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.dto.user.UserRegistrationForm;
import cz.rojik.backend.entity.UserEntity;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.exception.UserException;
import cz.rojik.backend.repository.UserRepository;
import cz.rojik.backend.service.EmailService;
import cz.rojik.controller.rest.UserController;
import cz.rojik.mock.SecurityHelperMock;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static cz.rojik.mock.MockConst.*;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 03. 02. 2019
 */
public class UserControllerTest extends MBMarkApplicationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    private SecurityHelperMock securityHelperMock;

    @Override
    public void setUp() {
        super.setUp();
        securityHelperMock = new SecurityHelperMock(securityHelper);
    }

    @Test
    public void getUserMockAdminTest() {
        securityHelperMock.mockAdmin();
        ResponseEntity<UserDTO> response = userController.getUser(1L);
        UserDTO user = response.getBody();

        assertUser(USER_ID_1, USER_EMAIL_1, USER_FIRSTNAME_1, USER_LASTNAME_1, USER_PASSWORD_1, user);
        Assert.assertTrue(user.getRoles().stream().allMatch(r -> r.getType().equalsIgnoreCase(ROLE_TYPE_1.getRoleType()) ||
                r.getType().equalsIgnoreCase(ROLE_TYPE_2.getRoleType())));
    }

    @Test
    public void getUserMockAdminTest2() {
        securityHelperMock.mockAdmin();
        ResponseEntity<UserDTO> response = userController.getUser(2L);
        UserDTO user = response.getBody();

        assertUser(USER_ID_2, USER_EMAIL_2, USER_FIRSTNAME_2, USER_LASTNAME_2, USER_PASSWORD_2, user);
    }

    @Test
    public void getUserMockUserTest() {
        securityHelperMock.mockUser();
        ResponseEntity<UserDTO> response = userController.getUser(2L);
        UserDTO user = response.getBody();

        assertUser(USER_ID_2, USER_EMAIL_2, USER_FIRSTNAME_2, USER_LASTNAME_2, USER_PASSWORD_2, user);
        Assert.assertTrue(user.getRoles().stream().allMatch(r -> r.getType().equalsIgnoreCase(ROLE_TYPE_2.getRoleType())));
    }

    @Test(expected = EntityNotFoundException.class)
    public void getUserMockUserExceptionTest() {
        securityHelperMock.mockUser();
        userController.getUser(3L);
    }

    @Test
    public void getUserMockDemoTest() {
        securityHelperMock.mockDemo();
        ResponseEntity<UserDTO> response = userController.getUser(3L);
        UserDTO user = response.getBody();

        assertUser(USER_ID_3, USER_EMAIL_3, USER_FIRSTNAME_3, USER_LASTNAME_3, USER_PASSWORD_3, user);
        Assert.assertTrue(user.getRoles().stream().allMatch(r -> r.getType().equalsIgnoreCase(ROLE_TYPE_3.getRoleType())));
    }

    @Test(expected = EntityNotFoundException.class)
    public void getUserMockDemoExceptionTest() {
        securityHelperMock.mockDemo();
        userController.getUser(2L);
    }

    @Test
    public void getUsers() {
        Optional<Boolean> enabled = Optional.empty();
        ResponseEntity<List<UserDTO>> response = userController.getUsers(enabled);
        List<UserDTO> entities = response.getBody();

        Assert.assertEquals(entities.size(), userRepository.count());
        Assert.assertTrue(entities.stream().allMatch(u -> u.getId().equals(USER_ID_1) ||
                u.getId().equals(USER_ID_2) ||
                u.getId().equals(USER_ID_3) ||
                u.getId().equals(USER_ID_4)));
    }

    @Test
    public void getUsersEnabled() {
        Optional<Boolean> enabled = Optional.of(true);
        ResponseEntity<List<UserDTO>> response = userController.getUsers(enabled);
        List<UserDTO> entities = response.getBody();

        Assert.assertEquals(entities.size(), 3);
        Assert.assertTrue(entities.stream().allMatch(u -> u.getId().equals(USER_ID_1) ||
                u.getId().equals(USER_ID_2) ||
                u.getId().equals(USER_ID_3)));
    }

    @Test
    public void getUsersNonEnabled() {
        Optional<Boolean> enabled = Optional.of(false);
        ResponseEntity<List<UserDTO>> response = userController.getUsers(enabled);
        List<UserDTO> entities = response.getBody();

        Assert.assertEquals(entities.size(), 1);
        Assert.assertTrue(entities.stream().allMatch(u -> u.getId().equals(USER_ID_4)));
    }

    @Test
    public void createUser() {
        String encodedPassword = "encoded-password";
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodedPassword);

        RoleDTO role = new RoleDTO().setType(ROLE_TYPE_2.getRoleType());
        role.setId(ROLE_ID_2);
        UserRegistrationForm form = new UserRegistrationForm()
                .setEmail("email@test.cz")
                .setFirstname("Marek")
                .setLastname("Rojik")
                .setPassword("test")
                .setRoles(Collections.singletonList(role));
        ResponseEntity<UserDTO> response = userController.createUser(form);
        UserDTO registeredUser = response.getBody();
        assertUser(5L, form.getEmail(), form.getFirstname(), form.getLastname(), encodedPassword, registeredUser);
        Assert.assertFalse(registeredUser.isEnabled());
        Assert.assertTrue(registeredUser.getRoles().stream().allMatch(r -> r.getType().equalsIgnoreCase(ROLE_TYPE_2.getRoleType())));
    }

    @Test
    public void createUserMockAdmin() {
        securityHelperMock.mockAdmin();
        String encodedPassword = "encoded-password";
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodedPassword);

        RoleDTO role = new RoleDTO().setType(ROLE_TYPE_2.getRoleType());
        role.setId(ROLE_ID_2);
        UserRegistrationForm form = new UserRegistrationForm()
                .setEmail("email@test.cz")
                .setFirstname("Marek")
                .setLastname("Rojik")
                .setPassword("test")
                .setRoles(Collections.singletonList(role));
        ResponseEntity<UserDTO> response = userController.createUser(form);
        UserDTO registeredUser = response.getBody();
        assertUser(5L, form.getEmail(), form.getFirstname(), form.getLastname(), encodedPassword, registeredUser);
        Assert.assertTrue(registeredUser.isEnabled());
        Assert.assertTrue(registeredUser.getRoles().stream().allMatch(r -> r.getType().equalsIgnoreCase(ROLE_TYPE_2.getRoleType())));
    }

    @Test
    public void createUserMockAdmin2() {
        securityHelperMock.mockAdmin();
        String encodedPassword = "encoded-password";
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodedPassword);

        RoleDTO roleAdmin = new RoleDTO().setType(ROLE_TYPE_1.getRoleType());
        roleAdmin.setId(ROLE_ID_1);
        RoleDTO roleUser = new RoleDTO().setType(ROLE_TYPE_2.getRoleType());
        roleUser.setId(ROLE_ID_2);
        UserRegistrationForm form = new UserRegistrationForm()
                .setEmail("email@test.cz")
                .setFirstname("Marek")
                .setLastname("Rojik")
                .setPassword("test")
                .setRoles(Arrays.asList(roleAdmin, roleUser));
        ResponseEntity<UserDTO> response = userController.createUser(form);
        UserDTO registeredUser = response.getBody();
        assertUser(5L, form.getEmail(), form.getFirstname(), form.getLastname(), encodedPassword, registeredUser);
        Assert.assertTrue(registeredUser.isEnabled());
        Assert.assertTrue(registeredUser.getRoles().stream().allMatch(r -> r.getType().equalsIgnoreCase(ROLE_TYPE_1.getRoleType()) ||
                r.getType().equalsIgnoreCase(ROLE_TYPE_2.getRoleType())));
    }

    @Test(expected = UserException.class)
    public void createUserValidateException() {
        String encodedPassword = "encoded-password";
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodedPassword);

        RoleDTO roleDemo = new RoleDTO().setType(ROLE_TYPE_3.getRoleType());
        roleDemo.setId(ROLE_ID_3);
        RoleDTO roleUser = new RoleDTO().setType(ROLE_TYPE_2.getRoleType());
        roleUser.setId(ROLE_ID_2);
        UserRegistrationForm form = new UserRegistrationForm()
                .setEmail("email@test.cz")
                .setFirstname("Marek")
                .setLastname("Rojik")
                .setPassword("test")
                .setRoles(Arrays.asList(roleDemo, roleUser));
        userController.createUser(form);
    }
//
    @Test(expected = UserException.class)
    public void createUserValidateException2() {
        String encodedPassword = "encoded-password";
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodedPassword);

        RoleDTO roleDemo = new RoleDTO().setType(ROLE_TYPE_1.getRoleType());
        roleDemo.setId(ROLE_ID_1);
        RoleDTO roleUser = new RoleDTO().setType(ROLE_TYPE_2.getRoleType());
        roleUser.setId(ROLE_ID_2);
        UserRegistrationForm form = new UserRegistrationForm()
                .setEmail("email@test.cz")
                .setFirstname("Marek")
                .setLastname("Rojik")
                .setPassword("test")
                .setRoles(Arrays.asList(roleDemo, roleUser));
        userController.createUser(form);
    }

    @Test(expected = UserException.class)
    public void createUserExistEmailException() {
        String encodedPassword = "encoded-password";
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodedPassword);

        RoleDTO roleUser = new RoleDTO().setType(ROLE_TYPE_2.getRoleType());
        roleUser.setId(ROLE_ID_2);
        UserRegistrationForm form = new UserRegistrationForm()
                .setEmail(USER_EMAIL_1)
                .setFirstname("Marek")
                .setLastname("Rojik")
                .setPassword("test")
                .setRoles(Collections.singletonList(roleUser));
        userController.createUser(form);
    }

    @Test
    public void updateUserMockAdmin() {
        securityHelperMock.mockAdmin();
        UserDTO form = new UserDTO()
                .setEmail("email@test.cz")
                .setFirstname("Marek")
                .setLastname("Rojik")
                .setEnabled(false);
        form.setId(USER_ID_3);
        ResponseEntity<UserDTO> response = userController.updateUser(USER_ID_3, form);
        UserDTO user = response.getBody();

        assertUser(USER_ID_3, form.getEmail(), form.getFirstname(), form.getLastname(), USER_PASSWORD_3, user);
        Assert.assertFalse(user.isEnabled());
    }

    @Test
    public void updateUserMockUser() {
        securityHelperMock.mockUser();
        UserDTO form = new UserDTO()
                .setEmail("email@test.cz")
                .setFirstname("Marek")
                .setLastname("Rojik")
                .setEnabled(false);
        form.setId(USER_ID_2);
        ResponseEntity<UserDTO> response = userController.updateUser(USER_ID_2, form);
        UserDTO user = response.getBody();

        assertUser(USER_ID_2, form.getEmail(), form.getFirstname(), form.getLastname(), USER_PASSWORD_2, user);
        Assert.assertFalse(user.isEnabled());
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateUserEntityNotFoundException() {
        UserDTO form = new UserDTO()
                .setEmail("email@test.cz")
                .setFirstname("Marek")
                .setLastname("Rojik")
                .setEnabled(true);
        form.setId(USER_ID_3);
        userController.updateUser(USER_ID_3, form);
    }

    @Test(expected = UserException.class)
    public void updateUserMockDemonExistEmailException() {
        securityHelperMock.mockDemo();
        UserDTO form = new UserDTO()
                .setEmail(USER_EMAIL_2)
                .setFirstname("Marek")
                .setLastname("Rojik")
                .setEnabled(true);
        form.setId(USER_ID_3);
        userController.updateUser(USER_ID_3, form);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateUserMockAdminEntityNotFoundException() {
        securityHelperMock.mockAdmin();
        UserDTO form = new UserDTO()
                .setEmail("email@test.cz")
                .setFirstname("Marek")
                .setLastname("Rojik")
                .setEnabled(true);
        form.setId(USER_ID_3);
        userController.updateUser(UNKNOWN_ID, form);
    }

    @Test
    public void delete() {
        userController.delete(USER_ID_4);

        List<UserEntity> users = userRepository.findAll();
        Assert.assertEquals(users.size(), 3);
        Assert.assertTrue(users.stream().noneMatch(u -> u.getId().equals(USER_ID_4)));
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteEntityNotFoundException() {
        userController.delete(UNKNOWN_ID);
    }

    private void assertUser(Long id, String email, String firstname, String lastname, String password, UserDTO user) {
        Assert.assertEquals(id, user.getId());
        Assert.assertEquals(email, user.getEmail());
        Assert.assertEquals(firstname, user.getFirstname());
        Assert.assertEquals(lastname, user.getLastname());
        Assert.assertEquals(password, user.getPassword());
    }

}