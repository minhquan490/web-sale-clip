package com.system.spring.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.system.spring.entity.Clip;
import com.system.spring.entity.User;

public interface ClipRepository extends JpaRepository<Clip, Long> {

	@Query("SELECT c FROM Clip c INNER JOIN FETCH c.categories WHERE c.user = :user")
	public Set<Clip> getClipsFromUser(@Param("user") User user);

	@Query("SELECT c FROM Clip c INNER JOIN FETCH c.categories WHERE c.name LIKE %?1")
	public List<Clip> getClipFromName(String clipName);

	@Query("SELECT c FROM Clip c WHERE c.name = :name")
	public Clip getClipByName(String name);

	@Query("SELECT c FROM Clip c INNER JOIN FETCH c.user WHERE c.id = :id")
	public Clip getClipById(long id);
}
