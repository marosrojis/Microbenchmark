package cz.rojik.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.rojik.backend.dto.BaseDTO;
import cz.rojik.backend.util.serialization.LocalDateTimeDeserializer;
import cz.rojik.backend.util.serialization.LocalDateTimeSerializer;
import cz.rojik.backend.auth.user.Login;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class UserDTO extends BaseDTO implements UserDetails {

	private static final long serialVersionUID = 1654651984613619846L;

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
	private boolean enabled;
	private long expires;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime created;

	@JsonIgnore
	private String password;

	@NotNull
	@NotEmpty
	private List<RoleDTO> roles = new ArrayList<>();

	public UserDTO() {

	}

	public UserDTO(Login user) {
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.expires = user.getExpires();
		this.roles = user.getRoles();
		this.created = LocalDateTime.now();
		this.firstname = user.getFirstName();
		this.lastname = user.getLastName();
		this.enabled = true;
		this.roles = user.getRoles();
	}

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();

        if (this.roles != null) {
            for (RoleDTO role : this.roles) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getType()));
            }
        }

        return authorities;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		UserDTO userDTO = (UserDTO) o;

		if (enabled != userDTO.enabled) return false;
		if (email != null ? !email.equals(userDTO.email) : userDTO.email != null) return false;
		if (firstname != null ? !firstname.equals(userDTO.firstname) : userDTO.firstname != null) return false;
		if (lastname != null ? !lastname.equals(userDTO.lastname) : userDTO.lastname != null) return false;
		if (created != null ? !created.equals(userDTO.created) : userDTO.created != null) return false;
		return !(password != null ? !password.equals(userDTO.password) : userDTO.password != null);

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
		result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
		result = 31 * result + (enabled ? 1 : 0);
		result = 31 * result + (created != null ? created.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UserDTO{" +
				"email='" + email + '\'' +
				", firstname='" + firstname + '\'' +
				", lastname='" + lastname + '\'' +
				", password='" + password + '\'' +
				", created=" + created +
				", enabled=" + enabled +
				'}';
	}

	@Override
	public String getUsername() {
		return email;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return enabled;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public List<RoleDTO> getRoles() {
		if (roles == null) {
			return new ArrayList<>();
		}
		return roles;
	}

	public UserDTO setRoles(List<RoleDTO> roles) {
		this.roles = roles;
		return this;
	}

	public UserDTO setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public UserDTO setCreated(LocalDateTime created) {
		this.created = created;
		return this;
	}

	public UserDTO setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public UserDTO setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getLastname() {
		return lastname;
	}

	public UserDTO setLastname(String lastname) {
		this.lastname = lastname;
		return this;
	}

	public String getFirstname() {
		return firstname;
	}

	public UserDTO setFirstname(String firstname) {
		this.firstname = firstname;
		return this;
	}

	public long getExpires() {
		return expires;
	}

	public UserDTO setExpires(long expires) {
		this.expires = expires;
		return this;
	}

}
