package com.system.spring.controller;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.spring.config.ApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.request.LoginRequest;
import com.system.spring.request.UserVo;
import com.system.spring.response.ServerResponse;
import com.system.spring.utils.JwtUtil;

@RestController
public class LoginController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping(path = ApiConfig.LOGIN_PATH)
	public ResponseEntity<ServerResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse resp)
			throws DisabledException, BadCredentialsException {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		String password = userDetails.getPassword();
		Set<String> roles = userDetails.getAuthorities().stream().map(r -> r.getAuthority())
				.collect(Collectors.toSet());
		boolean isEnabled = userDetails.isEnabled();
		boolean isPremium = userDetails.isPremium();
		UserVo user = new UserVo(username, password, roles, isEnabled, isPremium);
		String token = jwtUtil.generateToken(user);

		Cookie cookie = new Cookie("Authorization", token);
		cookie.setMaxAge(7 * 24 * 60 * 60);
		// cookie.setSecure(true); -> add this if using https or ssl
		cookie.setHttpOnly(true);
		cookie.setPath("/");

		resp.addCookie(cookie);

		return new ResponseEntity<ServerResponse>(
				new ServerResponse(LocalDateTime.now(), HttpStatus.OK, "Login success"), HttpStatus.OK);
	}
}
