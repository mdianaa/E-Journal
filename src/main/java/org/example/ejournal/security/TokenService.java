package org.example.ejournal.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TokenService {
	private final JwtEncoder jwtEncoder;
	private final JwtDecoder jwtDecoder;
	
	public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
		this.jwtEncoder = jwtEncoder;
		this.jwtDecoder = jwtDecoder;
	}
	
	public String generateJwt(Authentication auth){
		
		Instant now = Instant.now();
		
		List<String> roles = auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();
		
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.subject(auth.getName())
				.claim("roles", roles)
				.build();
		
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}


	//todo
	// unfinished
	public String extractUsername(String token) {
		Jwt jwt = jwtDecoder.decode(token);
		return jwt.getClaim("sub");
	}
}
