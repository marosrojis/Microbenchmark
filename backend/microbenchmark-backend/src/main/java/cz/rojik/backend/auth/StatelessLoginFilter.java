package cz.rojik.backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rojik.backend.dto.user.LoginDTO;
import cz.rojik.backend.dto.user.LoginPostDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.auth.user.UserDetailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Transactional
public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

	private final TokenAuthenticationService tokenAuthenticationService;
	private final UserDetailService userDetailService;

	public StatelessLoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService,
								UserDetailService userDetailService, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(urlMapping));
		this.userDetailService = userDetailService;
		this.tokenAuthenticationService = tokenAuthenticationService;
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Authentication auth = null;

		final LoginPostDTO login = new ObjectMapper().readValue(request.getInputStream(), LoginPostDTO.class);
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());
		try {
			auth = getAuthenticationManager().authenticate(loginToken);
		} catch (AuthenticationException e) {
			SecurityContextHolder.clearContext();
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
		return auth;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											FilterChain chain, Authentication authentication) throws IOException, ServletException {

		final UserDTO authenticatedUser = userDetailService.loadUserByUsername(authentication.getName());
		final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

		LoginDTO loginDTO = tokenAuthenticationService.createAuthentication(authenticatedUser);

		response.setHeader("Content-Type", "application/json");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), loginDTO);
		response.getWriter().flush();
		response.getWriter().close();
		// Add the authentication to the Security context
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
	}
}
