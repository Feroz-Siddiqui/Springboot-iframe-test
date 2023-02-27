package feroz.spring_phase_1.mvc;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration

public class MvcConfig implements WebMvcConfigurer{

	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		VersionResourceResolver versionResolver = new VersionResourceResolver().addContentVersionStrategy("/**/*.js",
				"/**/*.css", "/**/*.png");
		registry.addResourceHandler("/app_resources/**", "/favicon.ico", "/firebase-messaging-sw.js", "/manifest.json",
				"/salesken.png")
				.addResourceLocations("/assets/js/",
						"/assets/css/","/assets/img/","/")
				.setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES)).resourceChain(true)
				.addResolver(versionResolver);

	}	
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.APPLICATION_JSON);
	}
	
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setPrefix("pages/");
		bean.setSuffix(".jsp");
		return bean;
	}
}
