package com.system.spring.controller.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

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

import com.system.spring.config.ApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.entity.User;
import com.system.spring.request.UserUpdateRequest;
import com.system.spring.response.ClipHasPurchased;
import com.system.spring.response.MyClips;
import com.system.spring.response.UserInfo;
import com.system.spring.service.UserService;
import com.system.spring.utils.ExtensionFile;
import com.system.spring.utils.Random;

@Controller
@RequestMapping(ApiConfig.USER_PATH)
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ExtensionFile extensionFile;

	@PreAuthorize("hasAnyAuthority('viewer', 'admin', 'actor')")
	@GetMapping(ApiConfig.MY_INFO_PATH)
	public @ResponseBody ResponseEntity<?> myInfo(HttpServletRequest req) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = userDetails.getUser();
		UserInfo userInfo = new UserInfo(user.getId(), user.getFirstName() == null ? "" : user.getFirstName(),
				user.getLastName() == null ? "" : user.getLastName(), user.getEmail(),
				user.getGender() == null ? "" : user.getGender(),
				user.getBirthDate() == null ? "" : user.getBirthDate().toString(), user.getUsername(),
				user.getAvatar() == null ? ""
						: req.getContextPath() + ApiConfig.USER_PATH + ApiConfig.DISPLAY_USER_AVATAR + "/"
								+ user.getAvatar());
		return ResponseEntity.ok().body(userInfo);
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'actor')")
	@GetMapping(ApiConfig.MY_CLIP_PURCHASED)
	public @ResponseBody ResponseEntity<?> getMyClipPurchased() throws NullPointerException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User user = userService.getUserHasClipsPurchased(userDetails.getUserId());
		ClipHasPurchased clipsHasPurchased = new ClipHasPurchased(user.getClipsHaveBeenPurchased());
		return ResponseEntity.ok().body(clipsHasPurchased);
	}

	@PreAuthorize("hasAuthority('actor')")
	@GetMapping(ApiConfig.MY_CLIPS)
	public @ResponseBody ResponseEntity<?> getMyClips() throws NullPointerException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User user = userService.getUserClip(userDetails.getUserId());
		MyClips myClips = new MyClips(user.getClips());
		return ResponseEntity.ok().body(myClips);
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'admin', 'actor')")
	@PutMapping(ApiConfig.UPDATE_USER_INFO)
	public @ResponseBody ResponseEntity<?> updateMyInfo(@RequestBody UserUpdateRequest updateRequest)
			throws NullPointerException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User oldUser = userDetails.getUser();
		oldUser.setFirstName(updateRequest.getFirstName());
		oldUser.setLastName(updateRequest.getLastName());
		oldUser.setGender(updateRequest.getGender());
		oldUser.setBirthDate(Date.valueOf(updateRequest.getBirthDate()));
		if (userService.edit(oldUser)) {
			return ResponseEntity.ok().body("Update success !");
		} else {
			return ResponseEntity.badRequest().body("Update failure !");
		}
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'admin', 'actor')")
	@PutMapping(ApiConfig.UPLOAD_AVATAR_PATH)
	public @ResponseBody ResponseEntity<?> uploadAvatar(HttpServletRequest req) throws IOException, ServletException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User oldUser = userDetails.getUser();
		if (oldUser.getAvatar() != null) {
			Files.delete(Path.of(ApiConfig.UPLOAD_DATA_DIRECTORY + ApiConfig.USER_AVATAR + "/" + oldUser.getAvatar()));
		}
		Part avatar = req.getPart("avatar");
		String fileName = String.valueOf(Random.getRandomNumber());
		File dirFile = new File(ApiConfig.UPLOAD_DATA_DIRECTORY + ApiConfig.USER_AVATAR);
		File upload = File.createTempFile(fileName, extensionFile.generateExtension(avatar.getContentType()), dirFile);
		InputStream input = avatar.getInputStream();
		Files.copy(input, upload.toPath(), StandardCopyOption.REPLACE_EXISTING);
		oldUser.setAvatar(upload.getName());
		if (userService.edit(oldUser)) {
			return ResponseEntity.ok().body("Update success !");
		} else {
			return ResponseEntity.badRequest().body("Update failure !");
		}
	}
}