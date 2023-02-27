//package feroz.spring_phase_1.security;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.env.Environment;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
//import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
//import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
//import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
//import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.firewall.DefaultHttpFirewall;
//import org.springframework.security.web.firewall.HttpFirewall;
//
//@Configuration
//@PropertySource("classpath:application-oauth2.properties")
//public class OauthSecurityConfig {
//	Logger logger = LoggerFactory.getLogger(getClass());
//
//	private static List<String> clients = Arrays.asList("google", "facebook");
//	private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";
//	@Autowired
//	private Environment env;
//
//	@Bean
//	@Order(2)
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		
//		
//		http
//		
//		.csrf().disable().oauth2Login().loginPage("/auth").defaultSuccessUrl("/homesuccess");
//		return http.build();
//
//	}
//
//	@Bean
//	public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
//		return new HttpSessionOAuth2AuthorizationRequestRepository();
//	}
//
//	@Bean
//	public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
//		DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
//		return accessTokenResponseClient;
//	}
//
//	@Bean
//	public ClientRegistrationRepository clientRegistrationRepository() {
//		List<ClientRegistration> registrations = clients.stream().map(c -> getRegistration(c))
//				.filter(registration -> registration != null).collect(Collectors.toList());
//
//		return new InMemoryClientRegistrationRepository(registrations);
//	}
//
//	private ClientRegistration getRegistration(String client) {
//		String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");
//		logger.info("the client id for oauth2 app {} is {}", client, clientId);
//		if (clientId == null) {
//			return null;
//		}
//
//		String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
//		if (client.equals("google")) {
//			return CommonOAuth2Provider.GOOGLE.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
//		}
//		if (client.equals("facebook")) {
//			return CommonOAuth2Provider.FACEBOOK.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
//					.build();
//		}
//		return null;
//
//	}
//
//	@Bean
//	public HttpFirewall defaultHttpFirewall() {
//		return new DefaultHttpFirewall();
//	}
//}
