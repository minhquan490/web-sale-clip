package com.system.spring.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.spring.entity.Category;
import com.system.spring.entity.Clip;
import com.system.spring.exception.ResourceNotFoundException;
import com.system.spring.repository.CategoryRepository;
import com.system.spring.service.CategoryService;

@Service
@SuppressWarnings("deprecation")
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
	public boolean edit(Category category) {
		Category categoryExisting = categoryRepository.getById(category.getId());
		if (categoryExisting != null) {
			if (category.getClips() != null) {
				Set<Clip> clips = categoryExisting.getClips();
				if (clips == null) {
					clips = new HashSet<Clip>();
				}
				for (Clip clip : category.getClips()) {
					clips.add(clip);
				}
				categoryExisting.setClips(clips);
			}
			categoryRepository.save(categoryExisting);
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(Category category) {
		Category categoryExisting = categoryRepository.getById(category.getId());
		if (categoryExisting == null) {
			throw new ResourceNotFoundException("Category is not exist");
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
	public Category getCategoryByName(String categoryName) {
		return categoryRepository.getCategoryFromName(categoryName);
	}
}