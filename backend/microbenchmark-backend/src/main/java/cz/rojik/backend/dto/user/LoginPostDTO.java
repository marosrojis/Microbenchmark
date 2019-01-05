package cz.rojik.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginPostDTO {

	@Email
	private String email;
	@NotBlank
	private String password;

	public String getEmail() {
		return email.toLowerCase();
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
