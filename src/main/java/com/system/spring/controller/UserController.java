package com.system.spring.controller;

import java.sql.Date;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.spring.config.ApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.entity.User;
import com.system.spring.request.UserUpdateRequest;
import com.system.spring.response.ServerResponse;
import com.system.spring.response.UserInfo;
import com.system.spring.service.UserService;

@Controller
@RequestMapping(ApiConfig.USER_PATH)
public class UserController {

	@Autowired
	private UserService userService;

	@PreAuthorize("hasAnyAuthority('viewer', 'admin', 'actor')")
	@GetMapping(ApiConfig.MY_INFO_PATH)
	public @ResponseBody ResponseEntity<ServerResponse> myInfo(HttpServletRequest req) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userDetails.getUser();
		UserInfo userInfo = new UserInfo(user.getId(), user.getFirstName() == null ? "" : user.getFirstName(),
				user.getLastName() == null ? "" : user.getLastName(), user.getEmail(),
				user.getGender() == null ? "" : user.getGender(),
				user.getBirthDate() == null ? "" : user.getBirthDate().toString(), user.getUsername(),
				user.getAvatar() == null ? ""
						: req.getScheme() + "://" + req.getServerName() + ":" + String.valueOf(req.getServerPort())
								+ req.getContextPath() + ApiConfig.USER_PATH + ApiConfig.DISPLAY_USER_AVATAR + "/"
								+ user.getAvatar().substring(0, user.getAvatar().indexOf(".")));
		return ResponseEntity.ok().body(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, userInfo));
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'actor')")
	@GetMapping(ApiConfig.MY_CLIP_PURCHASED)
	public @ResponseBody ResponseEntity<ServerResponse> getMyClipPurchased() throws NullPointerException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User user = userService.getUserHasClipsPurchased(userDetails.getUserId());
		return ResponseEntity.ok()
				.body(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, user.getClipsHaveBeenPurchased()));
	}

	@PreAuthorize("hasAuthority('actor')")
	@GetMapping(ApiConfig.MY_CLIPS)
	public @ResponseBody ResponseEntity<ServerResponse> getMyClips() throws NullPointerException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.getUserClip(userDetails.getUserId());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, user.getClips()));
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'admin', 'actor')")
	@PutMapping(ApiConfig.UPDATE_USER_INFO)
	public @ResponseBody ResponseEntity<ServerResponse> updateMyInfo(@RequestBody UserUpdateRequest updateRequest,
			HttpServletRequest req) throws NullPointerException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User oldUser = userDetails.getUser();
		oldUser.setFirstName(updateRequest.getFirstName());
		oldUser.setLastName(updateRequest.getLastName());
		oldUser.setGender(updateRequest.getGender());
		oldUser.setBirthDate(Date.valueOf(updateRequest.getBirthDate()));
		if (userService.edit(oldUser)) {
			UserInfo info = new UserInfo(oldUser.getId(), oldUser.getFirstName(), oldUser.getLastName(),
					oldUser.getEmail(), oldUser.getGender(), oldUser.getBirthDate().toString(), oldUser.getUsername(),
					oldUser.getAvatar() == null ? ""
							: req.getScheme() + "://" + req.getServerName() + ":" + String.valueOf(req.getServerPort())
									+ req.getContextPath() + ApiConfig.USER_PATH + ApiConfig.DISPLAY_USER_AVATAR + "/"
									+ oldUser.getAvatar().substring(0, oldUser.getAvatar().indexOf(".")));
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.CREATED, info));
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, "Edit success !"));
	}

}
