package cz.rojik.auth;

import cz.rojik.auth.user.UserDetailService;
import cz.rojik.dto.user.LoginDTO;
import cz.rojik.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

@Service
public class TokenAuthenticationService {

	private static final String AUTH_HEADER_NAME = "Authorization";
	private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

	private final TokenHandler tokenHandler;

	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	public TokenAuthenticationService(@Value("${token.secret}") String secret) {
		tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
	}

	public LoginDTO createAuthentication(UserDTO user) {
		long expires = System.currentTimeMillis() + TEN_DAYS;
		user.setExpires(expires);
		String token = tokenHandler.createTokenForUser(user);
		return new LoginDTO(token, expires, user);
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		final String token = request.getHeader(AUTH_HEADER_NAME);
        return getAuthentication(token);
	}

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
