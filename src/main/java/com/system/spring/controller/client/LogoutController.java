package com.system.spring.controller.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.spring.config.PathApiConfig;

@RestController
public class LogoutController {

	@GetMapping(PathApiConfig.LOGOUT_PATH)
	public ResponseEntity<String> logout(HttpServletRequest req, HttpServletResponse resp) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(req, resp, auth);
			resp.addHeader("Authorization", "");
			return ResponseEntity.ok("Logout success");
		}
		return ResponseEntity.badRequest().body("Error !");
	}
}
