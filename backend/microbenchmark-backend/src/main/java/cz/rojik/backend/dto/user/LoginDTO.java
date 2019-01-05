package cz.rojik.backend.dto.user;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class LoginDTO {

	@NotBlank
	private String token;
	@NotEmpty
	private long expires;
	@NotNull
	private UserDTO user;

	public LoginDTO() {}

	public LoginDTO(String token, long expires, UserDTO user) {
		this.token = token;
		this.expires = expires;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
}