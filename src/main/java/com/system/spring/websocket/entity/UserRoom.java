package com.system.spring.websocket.entity;

import java.time.LocalDateTime;

public class UserRoom {

	private String roomId;
	private long userId;
	private String name;
	private LocalDateTime timeCreated;
	private String path;

	public UserRoom(String roomId, long userId, String name, LocalDateTime timeCreated, String path) {
		super();
		this.roomId = roomId;
		this.userId = userId;
		this.name = name;
		this.timeCreated = timeCreated;
		this.path = path;
	}

	public String getRoomId() {
		return roomId;
	}

	public long getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getTimeCreated() {
		return timeCreated;
	}

	public String getPath() {
		return path;
	}

}
