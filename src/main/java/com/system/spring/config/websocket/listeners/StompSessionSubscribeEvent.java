package com.system.spring.config.websocket.listeners;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.system.spring.websocket.entity.UserRoom;
import com.system.spring.websocket.entity.Viewer;

@Component
public class StompSessionSubscribeEvent implements ApplicationListener<SessionSubscribeEvent> {

	@Autowired
	@Qualifier("rooms")
	private ConcurrentHashMap<UserRoom, ConcurrentHashMap<String, Viewer>> rooms;

	@Autowired
	@Qualifier("userConnected")
	private ConcurrentHashMap<String, Viewer> userConnected;

	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		Viewer viewer = userConnected.get(accessor.getSessionId());
		viewer.setRoomId(accessor.getDestination().substring(accessor.getDestination().lastIndexOf("/") + 1,
				accessor.getDestination().length()));
		rooms.keySet().forEach(ur -> {
			if (ur.getRoomId().equals(viewer.getRoomId())) {
				ConcurrentHashMap<String, Viewer> viewers = rooms.get(ur);
				viewers.putIfAbsent(accessor.getSessionId(), viewer);
			}
		});
	}

}
