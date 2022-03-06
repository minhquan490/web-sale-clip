package com.system.spring.service;

import java.util.Collection;
import java.util.List;

import com.system.spring.entity.Category;
import com.system.spring.entity.Clip;

public interface CategoryService {

	boolean save(Category category);

	boolean delete(Category category);

	List<Category> getAllCategories();

	List<Category> getCategoriesForHomePage();

	Collection<Clip> getClipsFromCategory(String categoryName);

	Category get(String categoryName);

	Category get(long id);

	Category getCategoryToSaveClip(String categoryName);
}
