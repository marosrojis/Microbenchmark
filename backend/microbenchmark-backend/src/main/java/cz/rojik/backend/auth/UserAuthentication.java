package cz.rojik.backend.auth;

import cz.rojik.backend.dto.user.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Class contains information about logged user.
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class UserAuthentication implements Authentication {

	private final UserDTO user;
	private boolean authenticated = true;

	public UserAuthentication(UserDTO user) {
		this.user = user;
	}

	@Override
	public String getName() {
		return user.getUsername();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		return user.getPassword();
	}

	@Override
	public UserDTO getDetails() {
		return user;
	}

	@Override
	public Object getPrincipal() {
		return user.getUsername();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
}