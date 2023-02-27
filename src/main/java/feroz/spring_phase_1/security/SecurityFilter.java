package feroz.spring_phase_1.security;

import java.io.IOException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import feroz.spring_phase_1.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
    private JwtService jwtService;
	@Autowired
	MyUserDetailService myUserDetailService;

	
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String path = request.getRequestURI();

		logger.info("request getContextPath ---> : {}", path);
		if (path.endsWith(".css") || path.contains("error") || path.contains("oauth2") || path.endsWith(".js")
				|| path.contains("resources") || path.contains("auth") || path.contains("assets")
				|| path.contains("favicon.ico") || path.contentEquals("/product/authenticate") || path.contains("cdnjs.cloudflare.com")) {
			logger.info("request getContextPath ---> : {} bypassing", path);

			filterChain.doFilter(request, response);
		} else {
			
	        String authHeader = request.getHeader("Authorization");
	        String token = null;
	        String username = null;
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            token = authHeader.substring(7);
	            username = jwtService.extractUsername(token);
				logger.info("jwtService username ---> : {} ", username);

	        }
			
	        if (username != null && token!=null && request.getUserPrincipal() == null) {
	            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
	            if (jwtService.validateToken(token, userDetails)) {
	                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authToken);
					logger.info("jwtService Auth set ---> : {} ", username);

	                filterChain.doFilter(request, response);
	                return;
	                
	            }
	        }
	        
			Principal principal = SecurityContextHolder.getContext().getAuthentication();

			if (principal == null) {
				logger.info("principal null ---> : {} {} {}", path,request.getUserPrincipal(),SecurityContextHolder.getContext().getAuthentication());

				response.sendRedirect("/auth");
			} else {
				logger.info("{}", principal.toString());
				logger.info("{}", principal.getName());

				logger.info("principal found ---> : {} ", path);

				filterChain.doFilter(request, response);
			}

		}

	}
}
