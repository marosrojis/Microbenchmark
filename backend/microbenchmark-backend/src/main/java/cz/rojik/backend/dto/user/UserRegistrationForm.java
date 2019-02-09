package cz.rojik.backend.dto.user;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class UserRegistrationForm {

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String firstname;

    @NotNull
    @NotBlank
    private String lastname;

    @NotNull
    @NotEmpty
    private List<RoleDTO> roles;

    @NotNull
    @NotBlank
    private String password;

    public String getPassword() {
        return password;
    }

    public UserRegistrationForm setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserRegistrationForm setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public UserRegistrationForm setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegistrationForm setEmail(String email) {
        this.email = email;
        return this;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public UserRegistrationForm setRoles(List<RoleDTO> roles) {
        this.roles = roles;
        return this;
    }
}
