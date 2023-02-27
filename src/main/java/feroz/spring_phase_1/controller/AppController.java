package feroz.spring_phase_1.controller;

import java.util.Enumeration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AppController {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@GetMapping("/")
	public String viewHomePage() {
		return "index";
	}

	@RequestMapping("/auth")
	public String viewLoginPage(Model model, String error, String logout,HttpServletRequest request) {

		System.out.println("login called>>>>>>>>>> ");
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
		  String headerName = headerNames.nextElement();
		  System.out.println("Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
		}
		
		Enumeration<String> params = request.getParameterNames(); 
		while(params.hasMoreElements()){
		 String paramName = params.nextElement();
		 System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
		}
		if (error != null)
			model.addAttribute("errorMsg", "Your username and password are invalid.");

		if (logout != null)
			model.addAttribute("msg", "You have been logged out successfully.");

		return "login";
	}

	@GetMapping("/user")
	public String viewUserPage() {
		return "index";
	}

	@GetMapping("/index")
	public String viewAboutPage() {
		return "index";
	}

	@GetMapping("/process-form")
	public String getloginprocessing(ModelMap model, RedirectAttributes attr) {
		System.out.println("called>>>>>>>>>> ");
		return "index";

	}

	@GetMapping("/signup")
	public String viewSignUp(ModelMap model, RedirectAttributes attr) {
		System.out.println("called>>>>>>>>>> ");
		return "signup";

	}

	@GetMapping("/process-signup")
	public String viewProcessSignUp(ModelMap model, RedirectAttributes attr) {
		System.out.println("called>>>>>>>>>> ");
		return "signup";

	}

	@GetMapping("/homesuccess")
	public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication) {

		OAuth2AuthorizedClient client = authorizedClientService
				.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

		String userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
		logger.info("userInfoEndpointUri :::::::: {}", userInfoEndpointUri);
		if (!StringUtils.isEmpty(userInfoEndpointUri)) {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());

			HttpEntity<String> entity = new HttpEntity<String>("", headers);

			ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity,
					Map.class);
			Map userAttributes = response.getBody();
			model.addAttribute("name", userAttributes.get("name"));
		}

		return "index";
	}

	@PostMapping(path = "/signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String proceedSignUp(@RequestBody MultiValueMap<String, String> formData, RedirectAttributes attr) {

		String firstname = formData.get("firstname").toString();
		logger.info("firstnamefirstnamefirstnamefirstname :::::::{}", firstname);

		return "user";

	}

}
