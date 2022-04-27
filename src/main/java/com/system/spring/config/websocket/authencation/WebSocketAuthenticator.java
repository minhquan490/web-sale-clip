package com.system.spring.config.websocket.authencation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.system.spring.details.UserDetails;
import com.system.spring.request.UserVo;
import com.system.spring.service.UserService;
import com.system.spring.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class WebSocketAuthenticator {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	public UsernamePasswordAuthenticationToken getAuthenticationOrFail(final String TOKEN)
			throws AuthenticationException, ExpiredJwtException, SignatureException, MalformedJwtException,
			UnsupportedJwtException, IllegalAccessException {
		UserVo user = jwtUtil.getUserFromToken(TOKEN);
		UserDetails userDetails = (UserDetails) userService.loadUserByUsername(user.getUsername());
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
