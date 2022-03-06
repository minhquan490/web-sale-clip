package com.system.spring.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.system.spring.entity.Clip;
import com.system.spring.entity.User;
import com.system.spring.repository.ClipRepository;
import com.system.spring.service.ClipService;

@Service
public class ClipServiceImpl implements ClipService {

	@Autowired
	private ClipRepository clipRepository;

	@Override
	public boolean save(Clip clip) {
		if (clip != null) {
			clipRepository.save(clip);
			return true;
		}
		return false;
	}

	@Override
	public boolean edit(Clip clip) {
		Clip clipExisting = clipRepository.getReferenceById(clip.getId());
		if (clipExisting != null) {
			clipRepository.save(clipExisting);
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(Clip clip) {
		if (clip != null) {
			clipRepository.delete(clip);
			return true;
		}
		return false;
	}

	@Override
	public List<Clip> getAllClips() {
		return clipRepository.findAll();
	}

	@Override
	public List<Clip> getClipFromName(String clipName) {
		return clipRepository.getClipFromName(clipName);
	}

	@Override
	public Set<Clip> getClipsFromUser(User user) {
		return clipRepository.getClipsFromUser(user);
	}

	@Override
	public Clip get(long id) {
		return clipRepository.getClipById(id);
	}

	@Override
	public Clip get(String name) {
		return clipRepository.getClipByName(name);
	}

	@Override
	public List<Clip> getClipsForMainPage() {
		return clipRepository.findAll(PageRequest.ofSize(10)).toList();
	}

}
