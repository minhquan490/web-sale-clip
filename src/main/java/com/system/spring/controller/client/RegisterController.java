package com.system.spring.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.spring.config.PathApiConfig;
import com.system.spring.request.RegisterRequest;
import com.system.spring.request.UserVo;
import com.system.spring.service.UserService;

@RestController
public class RegisterController {

	@Autowired
	private UserService userService;

	@PostMapping(PathApiConfig.REGISTER_PATH)
	public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
		UserVo userVo = userService.getUserFromUsername(registerRequest.getUsername());
		if (userVo == null) {
			UserVo u = new UserVo();
			u.setUsername(registerRequest.getUsername());
			u.setPassword(registerRequest.getPassword());
			u.setEmail(registerRequest.getEmail());
			u.setEnabled(true);
			u.setPremium(false);
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