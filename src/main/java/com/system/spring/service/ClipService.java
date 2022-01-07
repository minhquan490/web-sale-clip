package com.system.spring.service;

import java.util.List;

import com.system.spring.entity.Clip;

public interface ClipService {

	boolean save(Clip clip);

	boolean edit(Clip clip);

	boolean delete(Clip clip);

	List<Clip> getAllClips();

	List<Clip> getClipFromName(String clipName);
}
