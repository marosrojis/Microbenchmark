package cz.rojik.backend.auth;

import com.google.gson.GsonBuilder;
import cz.rojik.backend.dto.ErrorDetailsDTO;
import cz.rojik.backend.exception.InvalidBearerTokenException;
import cz.rojik.backend.util.serialization.LocalDateTimeGsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Component
public class StatelessAuthenticationFilter extends GenericFilterBean {

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Authentication authentication;
		try {
			authentication = tokenAuthenticationService.getAuthentication(httpRequest);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
			SecurityContextHolder.getContext().setAuthentication(null);
		} catch (InvalidBearerTokenException e) {
			SecurityContextHolder.clearContext();

			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(LocalDateTime.now(), e.getMessage(),
					e.getDetails());
			httpResponse.getWriter().write(new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonSerializer()).create().toJson(errorDetails));
		}
	}
}