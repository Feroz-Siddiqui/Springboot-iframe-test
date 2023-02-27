package feroz.spring_phase_1.security;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
	Logger logger = LoggerFactory.getLogger(getClass());

	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

		logger.info("authentication ::::: {} ", authentication);
		logger.info("authentication.getPrincipal() ::::: {} ", authentication.getPrincipal().toString());
		final String name = authentication.getName();
		logger.info("CustomAuthenticationProvider ::::: " + name);
		final String password = authentication.getCredentials().toString();

		if ((name == null || password == null)) {
			throw new BadCredentialsException("Invalid username or password");
		}
		logger.info("CustomAuthenticationProvider ::::: " + authentication.getCredentials().toString());

		if (Objects.isNull(name)) {
			throw new BadCredentialsException("Invalid username or password");

		} else {

			final Authentication result = super.authenticate(authentication);
			return new UsernamePasswordAuthenticationToken(name, result.getCredentials().toString(),
					result.getAuthorities());

		}

	}
}
