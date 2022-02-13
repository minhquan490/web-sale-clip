package com.system.spring.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.system.spring.config.AuthenticationEntryPoint;
import com.system.spring.details.UserDetails;
import com.system.spring.exception.DisabledUserException;
import com.system.spring.exception.JwtMissingException;
import com.system.spring.exception.JwtTokenMalformedException;
import com.system.spring.exception.UnknowException;
import com.system.spring.request.UserVo;
import com.system.spring.service.UserService;
import com.system.spring.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = request.getHeader("Authorization");
			if (token == null) {
				throw new JwtMissingException("No JWT token found in request", null);
			}
			UserVo user = jwtUtil.getUserFromToken(token);
			UserDetails userDetails = (UserDetails) userService.loadUserByUsername(user.getUsername());
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			filterChain.doFilter(request, response);
		} catch (SignatureException e) {
			JwtTokenMalformedException exception = new JwtTokenMalformedException("Invalid JWT signature", e);
			authenticationEntryPoint.commence(request, response, exception);
		} catch (MalformedJwtException e) {
			JwtTokenMalformedException exception = new JwtTokenMalformedException("Invalid JWT token", e);
			authenticationEntryPoint.commence(request, response, exception);
		} catch (ExpiredJwtException e) {
			JwtTokenMalformedException exception = new JwtTokenMalformedException("Expired JWT token", e);
			authenticationEntryPoint.commence(request, response, exception);
		} catch (UnsupportedJwtException e) {
			JwtTokenMalformedException exception = new JwtTokenMalformedException("Unsupported JWT token", e);
			authenticationEntryPoint.commence(request, response, exception);
		} catch (IllegalArgumentException e) {
			throw new JwtMissingException("JWT claims string is empty", e);
		} catch (DisabledException e) {
			DisabledUserException exception = new DisabledUserException(
					"Your account is disable ! Contact to support if you need", e);
			authenticationEntryPoint.commence(request, response, exception);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new UnknowException("Unknow error", null);
		}

	}

}
