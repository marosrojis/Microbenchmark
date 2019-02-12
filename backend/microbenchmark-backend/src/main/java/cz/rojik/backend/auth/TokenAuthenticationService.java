package cz.rojik.backend.auth;

import cz.rojik.backend.dto.user.LoginDTO;
import cz.rojik.backend.auth.user.UserDetailService;
import cz.rojik.backend.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class TokenAuthenticationService {

	private static final String AUTH_HEADER_NAME = "Authorization";
	private static final long TOKEN_EXPIRE = 1000 * 60 * 60;

	private final TokenHandler tokenHandler;

	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	public TokenAuthenticationService(@Value("${token.secret}") String secret) {
		tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
	}

	/**
	 * Create authentication to user
	 * @param user logged user
	 * @return user with token
	 */
	public LoginDTO createAuthentication(UserDTO user) {
		long expires = System.currentTimeMillis() + TOKEN_EXPIRE;
		user.setExpires(expires);
		String token = tokenHandler.createTokenForUser(user);
		return new LoginDTO(token, expires, user);
	}

	/**
	 * Get user authentication (Spring security auth) based on Http request
	 * @return user authentication
	 */
	public Authentication getAuthentication(HttpServletRequest request) {
		final String token = request.getHeader(AUTH_HEADER_NAME);
        return getAuthentication(token);
	}

	/**
	 * Get user authentication (Spring security auth) based on user token
	 * @param token user's token
	 * @return user authentication
	 */
	public Authentication getAuthentication(String token) {
		if (token != null) {
			final UserDTO user = tokenHandler.parseUserFromToken(token);
			if (user != null) {
				return new UserAuthentication(userDetailService.loadUserByUsername(user.getEmail()));
			}
		}
		return null;
	}

	public TokenHandler getTokenHandler() {
		return tokenHandler;
	}
}
