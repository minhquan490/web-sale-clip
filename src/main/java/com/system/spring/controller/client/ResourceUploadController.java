package com.system.spring.controller.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;

import javax.servlet.ServletException;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.system.spring.config.ApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.entity.Clip;
import com.system.spring.entity.User;
import com.system.spring.exception.NotSupportException;
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
	@PutMapping(ApiConfig.UPLOAD_CLIP_PATH)
	public @ResponseBody ResponseEntity<?> uploadMyClips(HttpServletRequest req)
			throws NotSupportException, FileUploadException, IOException {
		boolean isMultiPart = ServletFileUpload.isMultipartContent(req);
		if (!isMultiPart) {
			throw new NotSupportException("Not supported !");
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User oldUser = userDetails.getUser();
		ServletFileUpload uploadFile = new ServletFileUpload();
		FileItemIterator iterator = uploadFile.getItemIterator(req);
		File upload = null;
		while (iterator.hasNext()) {
			FileItemStream item = iterator.next();
			InputStream input = item.openStream();
			if (!item.isFormField()) {
				String fileName = String.valueOf(Random.getRandomNumber());
				File dirFile = new File(ApiConfig.UPLOAD_DATA_DIRECTORY + ApiConfig.USER_VIDEO);
				upload = File.createTempFile(fileName, extensionFile.generateExtension(item.getContentType()), dirFile);
				Files.copy(input, upload.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		Clip clip = clipService.get(Long.parseLong(req.getHeader("id-video")));
		clip.setLink(upload.getAbsolutePath());
		Set<Clip> clips = oldUser.getClips();
		clips.add(clip);
		oldUser.setClips(clips);
		userService.edit(oldUser);
		return new ResponseEntity<String>("Upload successfully", HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'admin', 'actor')")
	@PostMapping(ApiConfig.UPLOAD_AVATAR_PATH)
	public ResponseEntity<String> uploadAvatar(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException, NotSupportException, FileUploadException {
		boolean isMultiPart = ServletFileUpload.isMultipartContent(req);
		if (!isMultiPart) {
			throw new NotSupportException("Not supported !");
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User oldUser = userDetails.getUser();
		if (oldUser.getAvatar() != null) {
			Files.delete(Path.of(ApiConfig.UPLOAD_DATA_DIRECTORY + ApiConfig.USER_AVATAR + "/" + oldUser.getAvatar()));
		}
		ServletFileUpload uploadFile = new ServletFileUpload();
		FileItemIterator iterator = uploadFile.getItemIterator(req);
		File upload = null;
		while (iterator.hasNext()) {
			FileItemStream item = iterator.next();
			InputStream input = item.openStream();
			if (!item.isFormField()) {
				String fileName = String.valueOf(Random.getRandomNumber());
				File dirFile = new File(ApiConfig.UPLOAD_DATA_DIRECTORY + ApiConfig.USER_AVATAR);
				upload = File.createTempFile(fileName, extensionFile.generateExtension(item.getContentType()), dirFile);
				Files.copy(input, upload.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		oldUser.setAvatar(upload.getName());
		userService.edit(oldUser);
		return new ResponseEntity<String>("Upload success", HttpStatus.CREATED);
	}
}
