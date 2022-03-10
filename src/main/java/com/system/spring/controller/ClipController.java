package com.system.spring.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.system.spring.config.ApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.entity.Category;
import com.system.spring.entity.Clip;
import com.system.spring.entity.Rate;
import com.system.spring.entity.User;
import com.system.spring.exception.ResourceNotFoundException;
import com.system.spring.exception.UnknowException;
import com.system.spring.request.ClipRequest;
import com.system.spring.request.RateRequest;
import com.system.spring.response.ClipResponse;
import com.system.spring.response.ServerResponse;
import com.system.spring.service.CategoryService;
import com.system.spring.service.ClipService;
import com.system.spring.utils.ExtensionFile;

@Controller
@RequestMapping(ApiConfig.CLIP_PATH)
public class ClipController {

	@Autowired
	private ClipService clipService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ExtensionFile extensionFile;

	@PreAuthorize("hasAuthority('actor')")
	@PostMapping(ApiConfig.CREATE_CLIP_INFOMATION)
	public @ResponseBody ResponseEntity<ServerResponse> createInfoClip(@RequestBody ClipRequest clipRequest)
			throws NullPointerException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDetails.getUser();
		Clip clip = new Clip();
		clip.setName(clipRequest.getClipName());
		if (clipRequest.getPrice() == null) {
			throw new NullPointerException();
		}
		clip.setPrice(Long.valueOf(clipRequest.getPrice()));
		Set<Category> categories = new HashSet<>();
		for (String categoryName : clipRequest.getCategories()) {
			Category category = categoryService.getCategoryToSaveClip(categoryName);
			categories.add(category);
		}
		clip.setCategories(categories);
		clip.setUser(user);
		clip.setEnabled(Boolean.getBoolean(clipRequest.isEnable()));
		if (clipService.save(clip)) {
			Clip clipSaved = clipService.get(clipRequest.getClipName());
			Set<String> categoryName = new HashSet<>();
			clipSaved.getCategories().forEach(c -> categoryName.add(c.getCategoryName()));
			ClipResponse clipResponse = new ClipResponse(clipSaved.getId(), clipSaved.getName(), clipSaved.getPrice(),
					categoryName);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.CREATED, clipResponse));
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.CONFLICT, "Create clip failure !"));
		}
	}

	@PreAuthorize("hasAnyAuthority('actor', 'admin')")
	@DeleteMapping(ApiConfig.REMOVE_CLIP + "/{id}")
	public @ResponseBody ResponseEntity<ServerResponse> deleteClip(@PathVariable(value = "id") String id,
			HttpServletRequest req) throws EntityNotFoundException, IOException {
		Clip clip = clipService.get(Long.valueOf(id));
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDetails.getUser();
		if (clipService.delete(clip)) {
			Files.delete(Path.of(ApiConfig.UPLOAD_DATA_DIRECTORY + ApiConfig.USER_VIDEO + "/" + user.getUsername() + "/"
					+ clip.getLink()));
			return ResponseEntity.ok(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, "Delete success"));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Delete failure"));
		}
	}

	@PreAuthorize("hasAuthority('actor')")
	@PutMapping(ApiConfig.UPDATE_CLIP_INFO + "/{id}")
	public @ResponseBody ResponseEntity<ServerResponse> updateClip(@PathVariable(value = "id") String id,
			@RequestBody ClipRequest clipRequest, HttpServletRequest req) throws EntityNotFoundException {
		Clip clip = clipService.get(Long.valueOf(id));
		clip.setName(clipRequest.getClipName());
		clip.setPrice(Long.valueOf(clipRequest.getPrice()));
		Set<Category> categories = clip.getCategories() == null ? new HashSet<>() : clip.getCategories();
		for (String categotyName : clipRequest.getCategories()) {
			categories.forEach(category -> {
				if (!category.getCategoryName().equals(categotyName)) {
					Category newCategory = categoryService.get(categotyName);
					categories.add(newCategory);
				}
			});
		}
		clip.setCategories(categories);
		clip.setEnabled(Boolean.getBoolean(clipRequest.isEnable()));
		if (clipService.edit(clip)) {
			Set<String> categoryName = new HashSet<>();
			clip.getCategories().forEach(c -> categoryName.add(c.getCategoryName()));
			ClipResponse clipResponse = new ClipResponse(clip.getId(), clip.getName(), clip.getPrice(), categoryName,
					req.getScheme() + "://" + req.getServerName() + ":" + String.valueOf(req.getServerPort())
							+ req.getContextPath() + ApiConfig.CLIP_PATH + ApiConfig.PLAY + "/"
							+ String.valueOf(clip.getId()));
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.CREATED, clipResponse));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.NOT_MODIFIED, "Update clip failure"));
		}
	}

	@GetMapping(ApiConfig.PLAY + "/{id}")
	public @ResponseBody ResponseEntity<StreamingResponseBody> playVideo(@PathVariable(value = "id") String id,
			@RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
		try {
			StreamingResponseBody responseBody;
			Clip clip = clipService.get(Long.valueOf(id));
			String filePathString = ApiConfig.UPLOAD_DATA_DIRECTORY + ApiConfig.USER_VIDEO + "/"
					+ clip.getUser().getUsername() + "/" + clip.getLink();
			Path filePath = Paths.get(filePathString);
			Long fileSize = Files.size(filePath);
			byte[] buffer = new byte[1024];
			final HttpHeaders headers = new HttpHeaders();
			String[] link = clip.getLink().split("\\.");
			String extension = "." + link[1];
			if (rangeHeader == null) {
				String contentType = "video/" + extensionFile.getContentType(extension);
				headers.add("Content-Type", contentType);
				headers.add("Content-Length", fileSize.toString());
				responseBody = os -> {
					RandomAccessFile file = new RandomAccessFile(filePathString, "r");
					try (file) {
						long pos = 0;
						file.seek(pos);
						while (pos < fileSize - 1) {
							file.read(buffer);
							os.write(buffer);
							pos += buffer.length;
						}
						os.flush();
					} catch (Exception e) {
						throw new UnknowException("Error", e);
					}
				};
				return ResponseEntity.status(HttpStatus.OK).headers(headers).body(responseBody);
			}
			String[] ranges = rangeHeader.split("\\-");
			Long rangeStart = Long.parseLong(ranges[0].substring(6));
			Long rangeEnd;
			if (ranges.length > 1) {
				rangeEnd = Long.parseLong(ranges[1]);
			} else {
				rangeEnd = fileSize - 1;
			}
			if (fileSize < rangeEnd) {
				rangeEnd = fileSize - 1;
			}
			String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
			String contentType = "video/" + extensionFile.getContentType(extension);
			headers.add("Content-Type", contentType);
			headers.add("Content-Length", contentLength);
			headers.add("Accept-Range", "bytes");
			headers.add("Content-Range", "bytes" + " " + rangeStart + "-" + rangeEnd + "/" + fileSize);
			final Long _rangeEnd = rangeEnd;
			responseBody = os -> {
				RandomAccessFile file = new RandomAccessFile(filePathString, "r");
				try (file) {
					long pos = rangeStart;
					file.seek(pos);
					while (pos < _rangeEnd) {
						file.read(buffer);
						os.write(buffer);
						pos += buffer.length;
					}
					os.flush();
				} catch (Exception e) {
					throw new UnknowException("Error", e);
				}
			};
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(headers).body(responseBody);
		} catch (FileNotFoundException e) {
			throw new ResourceNotFoundException("Not found", e);
		}
	}

	@PreAuthorize("hasAuthority('viewer')")
	@PutMapping(ApiConfig.RATE_CLIP + "/{idClip}")
	@ResponseBody
	public ResponseEntity<ServerResponse> rateClip(@PathVariable(name = "idClip") String idClip,
			@RequestBody RateRequest rateRequest, HttpServletRequest req) {
		Clip clip = clipService.get(Long.valueOf(idClip));
		Set<Rate> rates = clip.getRates() == null ? new HashSet<>() : clip.getRates();
		rates.add(new Rate(rateRequest.getValue(), rateRequest.getDecription()));
		Set<String> categories = new HashSet<>();
		for (Category category : clip.getCategories()) {
			categories.add(category.getCategoryName());
		}
		if (clipService.edit(clip)) {
			double rate = 0;
			int average = 0;
			int temp = 0;
			if (rates.isEmpty()) {
				rate = 0;
			} else {
				for (Rate r : rates) {
					average++;
					temp += r.getValue();
				}
				NumberFormat formatter = new DecimalFormat("#0.00");
				rate = Double.parseDouble(formatter.format((double) temp / average));
			}
			return ResponseEntity.ok(new ServerResponse(LocalDateTime.now(), HttpStatus.OK,
					new ClipResponse(clip.getId(), clip.getName(), clip.getPrice(), categories,
							req.getScheme() + "://" + req.getServerName() + ":" + String.valueOf(req.getServerPort())
									+ req.getContextPath() + ApiConfig.CLIP_PATH + ApiConfig.PLAY + "/"
									+ String.valueOf(clip.getId()),
							rate)));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Rate failure"));
		}
	}
}
