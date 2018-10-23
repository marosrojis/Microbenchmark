package cz.rojik.auth.user;

import cz.rojik.dto.user.RoleDTO;
import cz.rojik.dto.user.UserDTO;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Login {
	@NotEmpty
	@NotNull
	@Email
	private String email;
	@NotEmpty
	@NotNull
	private String password;
	private long expires;
	private List<RoleDTO> roles;
	@NotEmpty
	@NotNull
	private String firstName;
	@NotEmpty
	@NotNull
	private String lastName;
	public Login() {}

	public Login(UserDTO user) {
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.roles = user.getRoles();
		this.expires = user.getExpires();
		this.firstName = user.getFirstname();
		this.lastName = user.getLastname();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}
}
