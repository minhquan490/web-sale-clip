package com.system.spring.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.spring.config.ApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.entity.Clip;
import com.system.spring.entity.User;
import com.system.spring.service.ClipService;

@Controller
@RequestMapping(ApiConfig.USER_PATH)
public class MyClipsController {

	@Autowired
	private ClipService clipService;

	@PreAuthorize("hasAuthority('actor')")
	@GetMapping(ApiConfig.USER_VIDEO)
	public @ResponseBody ResponseEntity<?> getMyClips() throws NullPointerException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDetails.getUser();
		Set<Clip> clips = clipService.getClipsFromUser(user);
		return ResponseEntity.ok().body(clips);
	}
}
