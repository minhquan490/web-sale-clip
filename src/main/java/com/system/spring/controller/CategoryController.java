package com.system.spring.controller;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.spring.config.ApiConfig;
import com.system.spring.entity.Category;
import com.system.spring.request.CategoryRequest;
import com.system.spring.response.ServerResponse;
import com.system.spring.service.CategoryService;

@Controller
@RequestMapping(ApiConfig.CATEGORY_PATH)
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PreAuthorize("hasAuthority('admin')")
	@PostMapping(ApiConfig.CREATE_CATE)
	public @ResponseBody ResponseEntity<ServerResponse> createCategory(
			@RequestBody(required = false) CategoryRequest categoryRequest) {
		Category category = new Category();
		if (categoryRequest.getCategoryName() == null) {
			throw new NullPointerException();
		}
		category.setCategoryName(categoryRequest.getCategoryName());
		if (categoryService.save(category)) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.CREATED, "Create category success"));
		} else {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.SERVICE_UNAVAILABLE, "Create failure"));
		}
	}

	@PreAuthorize("hasAuthority('admin')")
	@DeleteMapping(ApiConfig.REMOVE_CATE + "/{id}")
	public @ResponseBody ResponseEntity<ServerResponse> delete(@PathVariable(name = "id") String id)
			throws NumberFormatException, EntityNotFoundException {
		Category category = categoryService.get(Long.parseLong(id));
		if (categoryService.delete(category)) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, "Delete category success"));
		} else {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
					new ServerResponse(LocalDateTime.now(), HttpStatus.SERVICE_UNAVAILABLE, "Delete category failure"));
		}
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'actor', 'admin')")
	@GetMapping(ApiConfig.ALL_CATE)
	public @ResponseBody ResponseEntity<ServerResponse> getAll() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, categoryService.getAllCategories()));
	}
}
