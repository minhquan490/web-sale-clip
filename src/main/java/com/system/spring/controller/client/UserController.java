package com.system.spring.controller.client;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.system.spring.details.UserDetails;
import com.system.spring.entity.User;
import com.system.spring.request.UserUpdateRequest;
import com.system.spring.response.ClipHasPurchased;
import com.system.spring.response.MyClips;
import com.system.spring.response.UserInfo;
import com.system.spring.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PreAuthorize("hasAnyAuthority('viewer', 'admin', 'actor')")
	@GetMapping("/my-info")
	public @ResponseBody ResponseEntity<?> myInfo() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userDetails.getUser();
		UserInfo userInfo = new UserInfo(user.getId(), user.getFirstName() == null ? "" : user.getFirstName(),
				user.getLastName() == null ? "" : user.getLastName(), user.getEmail(),
				user.getGender() == null ? "" : user.getGender(),
				user.getBirthDate() == null ? "" : user.getBirthDate().toString(), user.getUsername(),
				user.getAvatar() == null ? "" : user.getAvatar());
		return ResponseEntity.ok().body(userInfo);
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'actor')")
	@GetMapping("/my-clip-purchased")
	public @ResponseBody ResponseEntity<?> getMyClipPurchased() throws NullPointerException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User user = userService.getUserHasClipsPurchased(userDetails.getUserId());
		ClipHasPurchased clipsHasPurchased = new ClipHasPurchased(user.getClipsHaveBeenPurchased());
		return ResponseEntity.ok().body(clipsHasPurchased);
	}

	@PreAuthorize("hasAuthority('actor')")
	@GetMapping("/my-clips")
	public @ResponseBody ResponseEntity<?> getMyClips() throws NullPointerException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User user = userService.getUserClip(userDetails.getUserId());
		MyClips myClips = new MyClips(user.getClips());
		return ResponseEntity.ok().body(myClips);
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'admin', 'actor')")
	@PutMapping("/update")
	public @ResponseBody ResponseEntity<?> updateMyInfo(@RequestBody UserUpdateRequest updateRequest)
			throws NullPointerException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User oldUser = userDetails.getUser();
		oldUser.setFirstName(
				updateRequest.getFirstName() == null ? oldUser.getFirstName() : updateRequest.getFirstName());
		oldUser.setLastName(updateRequest.getLastName() == null ? oldUser.getLastName() : updateRequest.getLastName());
		oldUser.setGender(updateRequest.getGender() == null ? oldUser.getGender() : updateRequest.getGender());
		oldUser.setBirthDate(Date.valueOf(updateRequest.getBirthDate() == null ? oldUser.getBirthDate().toString()
				: updateRequest.getBirthDate()));
		if (userService.edit(oldUser)) {
			return ResponseEntity.ok().body("Update success !");
		} else {
			return ResponseEntity.badRequest().body("Update failure !");
		}

	}

}