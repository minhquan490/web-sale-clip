package com.system.spring.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.spring.config.ApiConfig;
import com.system.spring.entity.Category;
import com.system.spring.entity.Clip;
import com.system.spring.response.ServerResponse;
import com.system.spring.service.CategoryService;
import com.system.spring.service.ClipService;

@RestController
public class HomeController {

	@Autowired
	private ClipService clipService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping(path = ApiConfig.HOME_PATH)
	public ResponseEntity<ServerResponse> home() {
		List<Clip> allClips = clipService.getAllClips();
		List<Category> allCategories = categoryService.getCategoriesForHomePage();
		Map<String, Object> data = new HashMap<>();
		data.put("all-clip", allClips);
		data.put("all-categories", allCategories);
		return ResponseEntity.ok().body(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, data));
	}
}
