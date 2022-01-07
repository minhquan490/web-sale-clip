package com.system.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.system.spring.entity.Clip;

public interface ClipRepository extends JpaRepository<Clip, Long> {

	@Query("SELECT c FROM Clip c WHERE c.name LIKE %?1")
	public List<Clip> getClipFromName(String clipName);
}
