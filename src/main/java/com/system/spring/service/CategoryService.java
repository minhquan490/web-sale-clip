package com.system.spring.service;

import java.util.Collection;
import java.util.List;

import com.system.spring.entity.Category;
import com.system.spring.entity.Clip;

public interface CategoryService {

	boolean save(Category category);

	boolean edit(Category category);

	boolean delete(Category category);

	List<Category> getAllCategories();

	Collection<Clip> getClipsFromCategory(String categoryName);

	Category getCategoryByName(String categoryName);
}
