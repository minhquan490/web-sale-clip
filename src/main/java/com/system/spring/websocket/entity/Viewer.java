package com.system.spring.websocket.entity;

import com.system.spring.response.UserInfo;

public class Viewer {

	private UserInfo userInfo;
	private String[] roles;
	private String roomId;

	public Viewer(UserInfo userInfo, String[] roles, String roomId) {
		super();
		this.userInfo = userInfo;
		this.roles = roles;
		this.roomId = roomId;
	}

	public String[] getRoles() {
		return roles;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
}
