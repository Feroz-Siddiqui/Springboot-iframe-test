package feroz.spring_phase_1.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthSuccessHandler implements AuthenticationSuccessHandler {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		try {
			// removePreviousCookie(response);
			System.out.println("onAuthenticationSuccess >>>>>>>>>>>>>>"+authentication.toString());
			
			
			
			

			if(authentication instanceof OAuth2AuthenticationToken) {
				
				logger.error("{}",((OAuth2AuthenticationToken)authentication).getDetails().toString());
				logger.error("{}",((OAuth2AuthenticationToken)authentication).getAuthorizedClientRegistrationId());
				logger.error("{}",((OAuth2AuthenticationToken)authentication).getName());
				logger.error("{}",((OAuth2AuthenticationToken)authentication).getAuthorities().toString());
				
				switch (((OAuth2AuthenticationToken)authentication).getAuthorizedClientRegistrationId()) {
				case "google": {
					logger.error("it is google sign in .....");

					break;
				}
				case "twitch": {
					logger.error("it is twitch sign in .....");

					break;
				}

				}
				
				
				OAuth2AuthorizedClient client = authorizedClientService
						.loadAuthorizedClient(((OAuth2AuthenticationToken)authentication).getAuthorizedClientRegistrationId(),  ((OAuth2AuthenticationToken)authentication).getName());

				String userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
				logger.info("userInfoEndpointUri :::::::: {}", userInfoEndpointUri);
				
			}
		    
			
		    //addWelcomeCookie(gerUserName(authentication,request), response);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			 e.printStackTrace();
		}
	     request.getSession(false).setMaxInactiveInterval(604800);

		RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		redirectStrategy.sendRedirect(request, response, "/");
	}

}
