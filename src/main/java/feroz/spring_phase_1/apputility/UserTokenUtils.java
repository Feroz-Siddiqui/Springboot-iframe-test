package feroz.spring_phase_1.apputility;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class UserTokenUtils {
	private static String SECRET="7a427a6fb93f42829fee3605dc738a347a427a6fb93f42829fee3605dc738a34";
    private static final String AUTHORITIES_KEY = "authorities";

	public String generateToken(String username,String password) {
		Map<String,Object> 	claims = new HashMap<>();
		
		claims.put(AUTHORITIES_KEY, "USER_ROLE,ROLE_MANAGER");
		
		return createToken(claims,username);
	}

	private String createToken(Map<String, Object> claims, String username) {
		// TODO Auto-generated method stub
		return Jwts.builder().setClaims(claims).setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
				.signWith(getSignKey(),SignatureAlgorithm.HS256).compact();
	}

	private Key getSignKey() {
		byte [] key = Decoders.BASE64.decode(SECRET);
		
		// TODO Auto-generated method stub
		return Keys.hmacShaKeyFor(key);
	}
}
