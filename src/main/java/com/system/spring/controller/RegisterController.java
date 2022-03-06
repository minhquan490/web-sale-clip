package com.system.spring.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.spring.config.ApiConfig;
import com.system.spring.request.RegisterRequest;
import com.system.spring.request.UserVo;
import com.system.spring.response.ServerResponse;
import com.system.spring.service.UserService;

@RestController
public class RegisterController {

	@Autowired
	private UserService userService;

	@PostMapping(ApiConfig.REGISTER_PATH)
	public ResponseEntity<ServerResponse> register(@RequestBody RegisterRequest registerRequest)
			throws NullPointerException {
		UserVo userVo = userService.getUserFromUsername(registerRequest.getUsername());
		if (userVo == null) {
			UserVo u = new UserVo();
			u.setUsername(registerRequest.getUsername());
			u.setPassword(registerRequest.getPassword());
			u.setEmail(registerRequest.getEmail());
			u.setEnabled(true);
			u.setPremium(false);
			Set<String> roles = new HashSet<>();
			roles.add("viewer");
			u.setRoles(roles);
			userService.save(u);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, "User register success !"));
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.CONFLICT, "User is existed !"));
		}
	}
}
