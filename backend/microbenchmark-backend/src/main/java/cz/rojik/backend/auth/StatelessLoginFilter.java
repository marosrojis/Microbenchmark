package cz.rojik.backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rojik.backend.dto.user.LoginDTO;
import cz.rojik.backend.dto.user.LoginPostDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.auth.user.UserDetailService;
import org.springframework.security.authentication.AuthenticationManager;
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

@Transactional
public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

	private final TokenAuthenticationService tokenAuthenticationService;
	private final UserDetailService coworkUserDetailService;

	public StatelessLoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService,
								UserDetailService coworkUserDetailService, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(urlMapping));
		this.coworkUserDetailService = coworkUserDetailService;
		this.tokenAuthenticationService = tokenAuthenticationService;
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		final LoginPostDTO login = new ObjectMapper().readValue(request.getInputStream(), LoginPostDTO.class);
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());
		return getAuthenticationManager().authenticate(loginToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											FilterChain chain, Authentication authentication) throws IOException, ServletException {

		final UserDTO authenticatedUser = coworkUserDetailService.loadUserByUsername(authentication.getName());
		final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

		LoginDTO loginDTO = tokenAuthenticationService.createAuthentication(authenticatedUser);
//		coworkUserDetailService.update(authenticatedUser);

		response.setHeader("Content-Type", "application/json");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), loginDTO);
		response.getWriter().flush();
		response.getWriter().close();
		// Add the authentication to the Security context
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
	}
}
