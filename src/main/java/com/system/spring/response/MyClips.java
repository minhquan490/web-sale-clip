package com.system.spring.response;

import java.util.Set;

import com.system.spring.entity.Clip;

public class MyClips {

	private Set<Clip> myClips;

	public MyClips(Set<Clip> myClips) {
		super();
		this.myClips = myClips;
	}

	public Set<Clip> getMyClips() {
		return myClips;
	}

	public void setMyClips(Set<Clip> myClips) {
		this.myClips = myClips;
	}
}
