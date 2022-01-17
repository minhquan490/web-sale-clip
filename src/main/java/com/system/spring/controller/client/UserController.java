package com.system.spring.controller.client;

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
import com.system.spring.exception.MissingFieldException;
import com.system.spring.request.UserUpdateRequest;
import com.system.spring.response.UserInfo;
import com.system.spring.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PreAuthorize("hasAuthority('viewer')")
	@GetMapping("/my-info")
	public @ResponseBody ResponseEntity<?> myInfo() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userDetails.getUser();
		UserInfo userInfo;
		if (user.getFirstName().isBlank() && user.getLastName().isBlank() && user.getGender().isBlank()
				&& user.getAvatar().isBlank()) {
			userInfo = new UserInfo(user.getId(), "", "", user.getEmail(), "", "", user.getUsername(), "");
		} else {
			userInfo = new UserInfo(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
					user.getGender(), user.getBirthDate().toString(), user.getUsername(), user.getAvatar());
		}
		return ResponseEntity.ok().body(userInfo);
	}

	@PreAuthorize("hasRole('viewer') or hasRole('actor')")
	@GetMapping("my-clip-purchased")
	public @ResponseBody ResponseEntity<?> getMyClipPurchased() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User user = userService.getUserHasClipsPurchased(userDetails.getUserId());
		return ResponseEntity.ok().body(user);
	}

	@PreAuthorize("hasRole('actor')")
	@GetMapping("my-clips")
	public @ResponseBody ResponseEntity<?> getMyClips() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User user = userService.getUserClip(userDetails.getUserId());
		return ResponseEntity.ok().body(user);
	}

	@PreAuthorize("hasRole('anonymous')")
	@PutMapping("/update")
	public @ResponseBody ResponseEntity<?> updateMyInfo(@RequestBody UserUpdateRequest updateRequest) {
		if (updateRequest.getFirstName().isBlank() || updateRequest.getLastName().isBlank()
				|| updateRequest.getGender().isBlank() || updateRequest.getBirthDate().toString().isBlank()) {
			throw new MissingFieldException("Please insert full your information");
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User oldUser = userService.getUserInformation(userDetails.getUsername());
		oldUser.setFirstName(updateRequest.getFirstName());
		oldUser.setLastName(updateRequest.getLastName());
		oldUser.setGender(updateRequest.getGender());
		oldUser.setBirthDate(updateRequest.getBirthDate());
		userService.edit(oldUser);
		return ResponseEntity.ok().body("Update success !");
	}

}