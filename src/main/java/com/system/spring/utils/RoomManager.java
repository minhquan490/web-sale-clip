package com.system.spring.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.spring.entity.User;
import com.system.spring.websocket.entity.UserRoom;

@Component
public class RoomManager {

	@Autowired
	private BeanFactory beanFactory;

	@SuppressWarnings("unchecked")
	private Set<UserRoom> rooms = (Set<UserRoom>) beanFactory.getBean("userLiveRooms");

	@SuppressWarnings("unchecked")
	private Map<String, Map<String, User>> room = (Map<String, Map<String, User>>) beanFactory.getBean("room");

	public void createRoom(UserRoom userRoom) {
		room.put(userRoom.getRoomId(), new HashMap<>());
		rooms.add(userRoom);
	}

	public void deleteRoom(String roomId) {
		room.remove(roomId);
		rooms.forEach(r -> {
			if (r.getRoomId().equals(roomId)) {
				rooms.remove(r);
			}
		});
	}

	public void addUserToRoom(String ip, User user, String roomId) {
		Map<String, User> viewers = room.get(roomId);
		viewers.put(ip, user);
	}

	public void deleteUserToRoom(String ip, String roomId) {
		Map<String, User> viewers = room.get(roomId);
		viewers.remove(ip);
	}
}
