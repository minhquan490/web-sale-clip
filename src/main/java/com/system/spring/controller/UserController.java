package com.system.spring.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.spring.config.PathApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.request.LoginRequest;
import com.system.spring.request.RegisterRequest;
import com.system.spring.request.UserVo;
import com.system.spring.service.UserService;
import com.system.spring.utils.JwtUtil;

@RestController
public class UserController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping(path = PathApiConfig.LOGIN_PATH)
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest)
			throws DisabledException, BadCredentialsException {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		UserDetails userDetails = (UserDetails) userService.loadUserByUsername(loginRequest.getUsername());
		String username = userDetails.getUsername();
		String password = userDetails.getPassword();
		Set<String> roles = userDetails.getAuthorities().stream().map(r -> r.getAuthority())
				.collect(Collectors.toSet());
		UserVo user = new UserVo(username, password, roles, true);
		String token = jwtUtil.generateToken(user);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		return ResponseEntity.ok().headers(headers).body("Login success");
	}

	@PostMapping(path = PathApiConfig.REGISTER_PATH)
	public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
		UserVo userVo = userService.getUserFromUsername(registerRequest.getUsername());
		if (userVo == null) {
			UserVo u = new UserVo();
			u.setUsername(registerRequest.getUsername());
			u.setPassword(registerRequest.getPassword());
			u.setEmail(registerRequest.getEmail());
			u.setEnabled(true);
			if (registerRequest.getRoles().contains("admin")) {
				registerRequest.getRoles().remove("admin");
				u.setRoles(registerRequest.getRoles());
			}
			if (userService.save(u)) {
				return new ResponseEntity<String>("User successfully registered", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("User unsuccessfully registered", HttpStatus.OK);
			}

		} else {
			return new ResponseEntity<String>("User already exists", HttpStatus.CONFLICT);
		}
	}
}
