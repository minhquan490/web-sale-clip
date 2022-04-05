package com.system.spring.config.websocket;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import com.system.spring.details.UserDetails;
import com.system.spring.websocket.entity.UserRoom;
import com.system.spring.websocket.entity.Viewer;

@Component
public class WebSocketDecorate implements WebSocketHandlerDecoratorFactory {

	@Autowired
	private BeanFactory context;

	@SuppressWarnings("unchecked")
	private Set<UserRoom> userLiveRooms = (Set<UserRoom>) context.getBean("userLiveRooms");

	@SuppressWarnings("unchecked")
	private Map<UserRoom, Map<String, Viewer>> room = (Map<UserRoom, Map<String, Viewer>>) context.getBean("room");

	private boolean isActor = false;

	@Override
	public WebSocketHandler decorate(WebSocketHandler handler) {
		return new WebSocketMessageHandler() {

			private String roomId;

			@Override
			public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
				UserDetails userDetails = (UserDetails) session.getPrincipal();
				userDetails.getUser().getRoles().forEach(r -> {
					if (r.getRoleName().equals("actor")) {
						isActor = true;
					}
				});
				if (isActor) {
					userLiveRooms.forEach(ur -> {
						if (ur.getUserId() == userDetails.getUserId()) {
							room.remove(ur);
							userLiveRooms.remove(ur);
						}
					});
				} else {
					final UserRoom userRoom = null;
					userLiveRooms.forEach(ur -> {
						if (ur.getRoomId().equals(roomId)) {
							ur = userRoom;
						}
					});
					Map<String, Viewer> viewers = room.get(userRoom);
					viewers.remove(String.valueOf(userDetails.getUserId()));
				}
			}

			@Override
			protected void handlePingMessage(WebSocketSession session, PingMessage message) throws Exception {
				this.roomId = message.getPayload().toString();
			}
		};
	}

}