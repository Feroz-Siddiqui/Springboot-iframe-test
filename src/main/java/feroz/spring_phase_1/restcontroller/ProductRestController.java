package feroz.spring_phase_1.restcontroller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import feroz.spring_phase_1.dbmodel.Product;
import feroz.spring_phase_1.model.AuthRequest;
import feroz.spring_phase_1.services.JwtService;
import feroz.spring_phase_1.services.ProductService;

@RestController
@RequestMapping("product")
public class ProductRestController {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	ProductService productService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtService jwtService;
	
	
	@GetMapping("/welcome")
	@PreAuthorize("hasRole('admin')") 
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }
	
	
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public List<Product> listProducts() {
		return productService.fetchAllProducts();
	}
	

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_MANAGER')")
	public Product fetchProduct(@PathVariable Long id) {
		
		Optional<Product> product =productService.fetchProduct(id);
		
		return product.get();
	}
	
	
	@PostMapping("/authenticate")
	public String authenticate(@RequestBody AuthRequest authRequest) {
		Authentication auth =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		
		if(auth.isAuthenticated())
		return jwtService.generateToken(authRequest.getUsername());
		else
			throw new UsernameNotFoundException("Invalid user "+authRequest.getUsername());
	}
	
}
