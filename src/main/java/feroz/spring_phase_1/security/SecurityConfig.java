package feroz.spring_phase_1.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@PropertySource("classpath:application-oauth2.properties")
public class SecurityConfig {
	Logger logger = LoggerFactory.getLogger(getClass());

	List<String> publicApis = List.of("/resources/**", "/signup", "/pages/**", "/user/**", "/about", "/favicon.ico",
			"/login", "/logout", "/signup", "/cdn.jsdelivr.net/**", "/docs/**", "/signin.css");

	@Autowired
	MyUserDetailService myUserDetailService;

	private static List<String> clients = Arrays.asList("google", "facebook","twitch");
	private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";
	@Autowired
	private Environment env;
	
	@Autowired
	SecurityFilter securityFilter;
	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().disable()
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/app_resources/**", "/signup", "/favicon.ico", "/login", "/logout", "/signup",
								"/oauth2/authorization/**", "/pages/**", "/login/oauth2/code/**",
								"/login/oauth2/code/**","/twitch/**","/product/authenticate","/cdnjs.cloudflare.com/**")
						.permitAll().requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated())
				
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
				.formLogin(form -> form.loginPage("/auth").defaultSuccessUrl("/index")
						.failureHandler(authenticationfailure()).successHandler(new AuthSuccessHandler()).permitAll())
				.logout(out -> out.logoutUrl("/logout"))

				.authenticationProvider(authProvider())

				.oauth2Login().loginPage("/auth").defaultSuccessUrl("/homesuccess")
				.successHandler(new AuthSuccessHandler());
		
		
		http.headers().cacheControl();
		return http.build();
	}

	@Bean
	public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
		return new HttpSessionOAuth2AuthorizationRequestRepository();
	}

	@Bean
	public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
		DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
		return accessTokenResponseClient;
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		List<ClientRegistration> registrations = clients.stream().map(c -> getRegistration(c))
				.filter(registration -> registration != null).collect(Collectors.toList());

		return new InMemoryClientRegistrationRepository(registrations);
	}

	private ClientRegistration getRegistration(String client) {
		String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");
		logger.info("the client id for oauth2 app {} is {}", client, clientId);
		if (clientId == null) {
			return null;
		}

		String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
		if (client.equals("google")) {
			return CommonOAuth2Provider.GOOGLE.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
		}
		if (client.equals("facebook")) {
			return CommonOAuth2Provider.FACEBOOK.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
					.build();
		}
		
		if(client.equals("twitch")) {
			
			ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(client)
		            .clientId(clientId)
		            .clientSecret(clientSecret)
		            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
		            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
		            .redirectUri("http://localhost:8080/login/oauth2/code/twitch")
		            .scope("openid", "user:read:email")
		            .authorizationUri("https://id.twitch.tv/oauth2/authorize")
		            .tokenUri("https://id.twitch.tv/oauth2/token")
		            .userInfoUri("https://id.twitch.tv/oauth2/userinfo")
		            .jwkSetUri("https://id.twitch.tv/oauth2/keys")
		            .clientName(client)
		            .build();
			return clientRegistration;
		}
		return null;

	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
		authProvider.setUserDetailsService(myUserDetailService);
		authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		// authProvider.setPostAuthenticationChecks(differentLocationChecker);
		return authProvider;
	}

	private AuthenticationFailureHandler authenticationfailure() {
		// TODO Auto-generated method stub
		return new AuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				// TODO Auto-generated method stub
				Enumeration<String> params = request.getParameterNames();
				while (params.hasMoreElements()) {
					String paramName = params.nextElement();
					System.out
							.println("Parameter Name - " + paramName + ", Value - " + request.getParameter(paramName));
				}
				String username = request.getParameter("username");
				String password = request.getParameter("password");

				System.out.println("username >>>>>>>>>>>>>>" + username);
				System.out.println("password >>>>>>>>>>>>>>" + password);

				request.setAttribute("message", "Hello world");

				if (username == null || username.equalsIgnoreCase("")) {
					System.out.println("username is blank");

					request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, "username is balnk");
					// request.getRequestDispatcher("/login?error=username is
					// balnk").forward(request, response);
					response.sendRedirect("/auth");
					return;
				}

				if (password == null || password.equalsIgnoreCase("")) {
					System.out.println("passowrd is blank");
					request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, "password is balnk");
					response.sendRedirect("/auth");
					return;

					// request.getRequestDispatcher("/login").forward(request, response);
				}

				request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
						"invalid username or password");

				response.sendRedirect("/auth");
				return;

			}
		};
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
