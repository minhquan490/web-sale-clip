package com.system.spring.response;

import java.util.Set;

import com.system.spring.entity.Clip;

public class ClipHasPurchased {

	private Set<Clip> clipsHasPurchased;

	public ClipHasPurchased(Set<Clip> clipsHasPurchased) {
		super();
		this.clipsHasPurchased = clipsHasPurchased;
	}

	public Set<Clip> getClipsHasPurchased() {
		return clipsHasPurchased;
	}

	public void setClipsHasPurchased(Set<Clip> clipsHasPurchased) {
		this.clipsHasPurchased = clipsHasPurchased;
	}
}
