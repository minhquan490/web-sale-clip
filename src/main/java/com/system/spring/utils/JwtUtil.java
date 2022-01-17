package com.system.spring.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.system.spring.request.UserVo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtil {

	private final String JWT_SECRET_KEY = "LEN";
	private final long TOKEN_VALIDITY = 60 * 30 * 1000;
	private String jwtSecretEncodedKey = Base64.getEncoder().encodeToString(JWT_SECRET_KEY.getBytes());

	public UserVo getUserFromToken(final String TOKEN) throws Exception {
		Claims body = validateToken(TOKEN);
		UserVo user = new UserVo(body.getSubject(), (String) body.get("email"),
				Arrays.asList(body.get("roles").toString().split(",")).stream().map(r -> new String(r))
						.collect(Collectors.toSet()),
				Boolean.getBoolean(body.get("isEnabled").toString()),
				Boolean.getBoolean(body.get("isPremium").toString()));
		return user;
	}

	public String generateToken(UserVo userRequest) {
		Claims claims = Jwts.claims().setSubject(userRequest.getUsername());
		claims.put("roles", userRequest.getRoles());
		claims.put("email", userRequest.getEmail());
		claims.put("isEnabled", userRequest.isEnabled());
		claims.put("isPremium", userRequest.isPremium());
		long now = ZonedDateTime.now(ZoneId.of("UTC+7")).toInstant().toEpochMilli();
		long expire = now + TOKEN_VALIDITY;
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date(now)).setExpiration(new Date(expire))
				.signWith(SignatureAlgorithm.HS512, jwtSecretEncodedKey).compact();
	}

	private Claims validateToken(final String TOKEN) throws SignatureException, MalformedJwtException,
			ExpiredJwtException, UnsupportedJwtException, IllegalAccessException {
		return Jwts.parser().setSigningKey(jwtSecretEncodedKey).parseClaimsJws(TOKEN).getBody();
	}
}
