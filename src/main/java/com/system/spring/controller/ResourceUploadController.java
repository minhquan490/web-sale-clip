package com.system.spring.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.spring.config.ApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.entity.Clip;
import com.system.spring.entity.User;
import com.system.spring.exception.NotSupportException;
import com.system.spring.response.ServerResponse;
import com.system.spring.service.ClipService;
import com.system.spring.service.UserService;
import com.system.spring.utils.ExtensionFile;
import com.system.spring.utils.Random;

@RestController
@RequestMapping(ApiConfig.RESOURCE_PATH)
public class ResourceUploadController {

	@Autowired
	private UserService userService;

	@Autowired
	private ClipService clipService;

	@Autowired
	private ExtensionFile extensionFile;

	@PreAuthorize("hasAuthority('actor')")
	@PostMapping(ApiConfig.UPLOAD_CLIP_PATH)
	public @ResponseBody ResponseEntity<ServerResponse> uploadMyClips(HttpServletRequest req)
			throws NotSupportException, FileUploadException, IOException {
		boolean isMultiPart = ServletFileUpload.isMultipartContent(req);
		if (!isMultiPart) {
			throw new NotSupportException("Not supported !", null);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User oldUser = userDetails.getUser();
		ServletFileUpload uploadFile = new ServletFileUpload();
		FileItemIterator iterator = uploadFile.getItemIterator(req);
		File upload;
		Clip clip = null;
		while (iterator.hasNext()) {
			FileItemStream item = iterator.next();
			try (InputStream input = item.openStream()) {
				if (!item.isFormField()) {
					String fileName = String.valueOf(Random.getRandomNumber());
					File dirFile = new File(ApiConfig.UPLOAD_DATA_DIRECTORY + "/" + ApiConfig.USER_VIDEO + "/"
							+ oldUser.getUsername() + "/");
					if (!dirFile.exists()) {
						dirFile.mkdir();
					}
					upload = File.createTempFile(fileName, extensionFile.generateExtension(item.getContentType()),
							dirFile);
					Files.copy(input, upload.toPath(), StandardCopyOption.REPLACE_EXISTING);
					clip = clipService.get(Long.parseLong(item.getFieldName()));
					clip.setLink(upload.getName());
				}
			}
		}
		if (clip == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, "Unknow error"));
		}
		clipService.edit(clip);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ServerResponse(LocalDateTime.now(), HttpStatus.CREATED, "Upload success"));
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'admin', 'actor')")
	@PostMapping(ApiConfig.UPLOAD_AVATAR_PATH)
	public ResponseEntity<ServerResponse> uploadAvatar(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, NotSupportException, FileUploadException {
		boolean isMultiPart = ServletFileUpload.isMultipartContent(req);
		if (!isMultiPart) {
			throw new NotSupportException("Not supported !", null);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User oldUser = userDetails.getUser();
		if (oldUser.getAvatar() != null) {
			try {
				Files.delete(Path.of(ApiConfig.UPLOAD_DATA_DIRECTORY + ApiConfig.USER_AVATAR + "/"
						+ oldUser.getUsername() + "/" + oldUser.getAvatar()));
			} catch (NoSuchFileException e) {

			}
		}
		ServletFileUpload uploadFile = new ServletFileUpload();
		FileItemIterator iterator = uploadFile.getItemIterator(req);
		File upload;
		while (iterator.hasNext()) {
			FileItemStream item = iterator.next();
			try (InputStream input = item.openStream()) {
				if (!item.isFormField()) {
					String fileName = String.valueOf(Random.getRandomNumber());
					File dirFile = new File(ApiConfig.UPLOAD_DATA_DIRECTORY + "/" + ApiConfig.USER_AVATAR + "/"
							+ oldUser.getUsername() + "/");
					if (!dirFile.exists()) {
						dirFile.mkdir();
					}
					upload = File.createTempFile(fileName, extensionFile.generateExtension(item.getContentType()),
							dirFile);
					Files.copy(input, upload.toPath(), StandardCopyOption.REPLACE_EXISTING);
					oldUser.setAvatar(upload.getName());
				}
			}
		}
		userService.edit(oldUser);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ServerResponse(LocalDateTime.now(), HttpStatus.CREATED, "Upload success"));
	}
}
