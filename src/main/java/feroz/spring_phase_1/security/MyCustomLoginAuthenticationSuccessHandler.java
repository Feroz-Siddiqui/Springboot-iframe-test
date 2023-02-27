package feroz.spring_phase_1.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class MyCustomLoginAuthenticationSuccessHandler  implements AuthenticationSuccessHandler {
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		logger.debug("MyCustomLoginAuthenticationSuccessHandler ::::: "+username);

		//addWelcomeCookie(gerUserName(authentication,request), response);
		redirectStrategy.sendRedirect(request, response, "/maindashboard?user=" + "a");

		final HttpSession session = request.getSession(false);
		if (session != null) {
			session.setMaxInactiveInterval(120);
//			if (authentication.getPrincipal() instanceof User) {
//				username = ((User) authentication.getPrincipal()).getEmail();
//			} else {
//				username = authentication.getName();
//			}
//
//			LoggedUser user = new LoggedUser(username, activeUserStore);
//			session.setAttribute("user", user);
		}
		clearAuthenticationAttributes(request);
	}

	
	protected void clearAuthenticationAttributes(final HttpServletRequest request) {
		final HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

}
