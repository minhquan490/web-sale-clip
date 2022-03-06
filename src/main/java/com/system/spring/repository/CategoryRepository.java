package com.system.spring.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.system.spring.entity.Category;
import com.system.spring.entity.Clip;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query("SELECT c FROM Category c INNER JOIN FETCH c.clips WHERE c.categoryName = :name")
	public Collection<Clip> getClipsFromCategory(@Param("name") String categoryName);

	@Query("SELECT c FROM Category c INNER JOIN FETCH c.clips WHERE c.categoryName = :name")
	public Category getCategoryFromName(@Param("name") String name);

	@Query("SELECT c FROM Category c WHERE c.categoryName = :name")
	public Category getCategoryToSaveClip(@Param("name") String name);
}
