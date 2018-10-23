package cz.rojik.error;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MBenchmarkAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			AccessDeniedException e) throws IOException, ServletException {

		httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "User is not allowed access this url");
	}
}
