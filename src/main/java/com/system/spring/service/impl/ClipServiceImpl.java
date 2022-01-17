package com.system.spring.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.spring.entity.Category;
import com.system.spring.entity.Clip;
import com.system.spring.entity.User;
import com.system.spring.exception.ResourceNotFoundException;
import com.system.spring.repository.ClipRepository;
import com.system.spring.service.ClipService;

@Service
@SuppressWarnings("deprecation")
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
		Clip clipExisting = clipRepository.getById(clip.getId());
		if (clipExisting != null) {
			clipExisting.setName(clip.getName());
			clipExisting.setLink(clip.getLink());
			clipExisting.setPrice(clip.getPrice());
			if (clip.getUsersPurchased() != null) {
				Set<User> setUserPurchased = clipExisting.getUsersPurchased();
				if (setUserPurchased == null) {
					setUserPurchased = new HashSet<User>();
				}
				for (User user : clip.getUsersPurchased()) {
					setUserPurchased.add(user);
				}
				clipExisting.setUsersPurchased(setUserPurchased);
			}
			if (clip.getCategories() != null) {
				Set<Category> setCategories = clipExisting.getCategories();
				if (setCategories == null) {
					setCategories = new HashSet<Category>();
				}
				for (Category category : clip.getCategories()) {
					setCategories.add(category);
				}
				clipExisting.setCategories(null);
			}
			clipRepository.save(clipExisting);
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(Clip clip) {
		if (clip != null) {
			Clip clipExisting = clipRepository.getById(clip.getId());
			if (clipExisting == null) {
				throw new ResourceNotFoundException("No clip to delete");
			}
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

}
