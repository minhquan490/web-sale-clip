package com.system.spring.config.websocket.listeners;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.system.spring.websocket.entity.UserRoom;
import com.system.spring.websocket.entity.Viewer;

@Component
public class StompSessionDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

	@Autowired
	@Qualifier("rooms")
	private ConcurrentHashMap<UserRoom, ConcurrentHashMap<String, Viewer>> rooms;

	@Autowired
	@Qualifier("userConnected")
	private ConcurrentHashMap<String, Viewer> userConnected;

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		if (userConnected.get(event.getSessionId()) != null
				&& userConnected.get(event.getSessionId()).getRoomId() != null) {
			StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
			Viewer viewer = userConnected.get(event.getSessionId());
			String roomId = viewer.getRoomId();
			rooms.keySet().forEach(ur -> {
				if (rooms.get(ur).isEmpty()) {
					rooms.remove(ur);
				} else if (ur.getRoomId().equals(roomId)) {
					ConcurrentHashMap<String, Viewer> viewers = rooms.get(ur);
					viewers.remove(accessor.getSessionId());
				}
			});
		}
		userConnected.remove(event.getSessionId());
	}
}
