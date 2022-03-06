package com.system.spring.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.system.spring.entity.Category;
import com.system.spring.entity.Clip;
import com.system.spring.exception.ResourceNotFoundException;
import com.system.spring.repository.CategoryRepository;
import com.system.spring.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public boolean save(Category category) {
		if (category != null) {
			categoryRepository.save(category);
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(Category category) {
		Category categoryExisting = categoryRepository.getReferenceById(category.getId());
		if (categoryExisting == null) {
			throw new ResourceNotFoundException("Category is not exist", null);
		}
		categoryRepository.delete(category);
		return true;
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Collection<Clip> getClipsFromCategory(String categoryName) {
		return categoryRepository.getClipsFromCategory(categoryName);
	}

	@Override
	public Category get(String categoryName) {
		return categoryRepository.getCategoryFromName(categoryName);
	}

	@Override
	public Category get(long id) {
		return categoryRepository.getReferenceById(id);
	}

	@Override
	public List<Category> getCategoriesForHomePage() {
		Pageable page = PageRequest.ofSize(5);
		return categoryRepository.findAll(page).toList();
	}

	@Override
	public Category getCategoryToSaveClip(String categoryName) {
		return categoryRepository.getCategoryToSaveClip(categoryName);
	}
}