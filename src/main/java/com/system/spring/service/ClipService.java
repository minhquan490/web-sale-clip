package com.system.spring.service;

import java.util.List;
import java.util.Set;

import com.system.spring.entity.Clip;
import com.system.spring.entity.User;

public interface ClipService {

	boolean save(Clip clip);

	boolean edit(Clip clip);

	boolean delete(Clip clip);

	Clip get(long id);

	Clip get(String name);

	List<Clip> getAllClips();

	List<Clip> getClipsForMainPage();

	List<Clip> getClipFromName(String clipName);

	Set<Clip> getClipsFromUser(User user);
}
