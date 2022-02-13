package com.system.spring.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.system.spring.config.ApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.entity.User;
import com.system.spring.exception.ResourceNotFoundException;

@Controller
public class DisplayAvatarController {

	private String path = ApiConfig.UPLOAD_DATA_DIRECTORY + ApiConfig.USER_AVATAR + "/";

	@PreAuthorize("hasAnyAuthority('viewer', 'admin', 'actor')")
	@GetMapping(ApiConfig.DISPLAY_USER_AVATAR + "/{avatarName}")
	public void displayAvatar(@PathVariable("avatarName") String avatarName, HttpServletResponse resp)
			throws IOException, ServletException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDetails.getUser();
		if (user.getAvatar().contains(avatarName)) {
			File avatar = new File(path, URLDecoder.decode(user.getAvatar(), "UTF-8"));
			String contentType = Files.probeContentType(avatar.toPath());
			resp.reset();
			resp.setContentType(contentType);
			resp.setHeader("Content-Length", String.valueOf(avatar.length()));
			Files.copy(avatar.toPath(), resp.getOutputStream());
		} else {
			throw new ResourceNotFoundException("invalid avatar", null);
		}
	}

}
