package com.system.spring.authentication;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.system.spring.request.UserVo;
import com.system.spring.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class WebSocketAuthenticator {

	@Autowired
	private JwtUtil jwtUtil;

	public UsernamePasswordAuthenticationToken getAuthenticationOrFail(final String TOKEN)
			throws AuthenticationException, ExpiredJwtException, SignatureException, MalformedJwtException,
			UnsupportedJwtException, IllegalAccessException {
		UserVo userVo = jwtUtil.getUserFromToken(TOKEN);
		return new UsernamePasswordAuthenticationToken(userVo.getUsername(), null,
				Collections.singleton((GrantedAuthority) userVo.getRoles()));
	}
}
