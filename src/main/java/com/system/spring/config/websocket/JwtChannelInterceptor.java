package com.system.spring.config.websocket;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.system.spring.config.ApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.entity.User;
import com.system.spring.response.UserInfo;
import com.system.spring.websocket.entity.UserRoom;
import com.system.spring.websocket.entity.Viewer;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

	@Autowired
	private BeanFactory context;

	@Autowired
	private WebSocketSession session;

	@SuppressWarnings("unchecked")
	private Set<UserRoom> userLiveRoom = (Set<UserRoom>) context.getBean("userLiveRooms");

	@SuppressWarnings("unchecked")
	private Map<UserRoom, Map<String, Viewer>> room = (Map<UserRoom, Map<String, Viewer>>) context.getBean("room");

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
			String roomId = accessor.getDestination().substring(accessor.getDestination().lastIndexOf("/") + 1,
					accessor.getDestination().length());
			UserDetails userDetails = (UserDetails) session.getPrincipal();
			User user = userDetails.getUser();
			UserInfo userInfo = new UserInfo(user.getId(), user.getFirstName() == null ? "" : user.getFirstName(),
					user.getLastName() == null ? "" : user.getLastName(), user.getEmail(),
					user.getGender() == null ? "" : user.getGender(),
					user.getBirthDate() == null ? "" : user.getBirthDate().toString(), user.getUsername(),
					user.getAvatar() == null ? ""
							: session.getAttributes().get("serverScheme") + "://"
									+ session.getAttributes().get("serverName") + ":"
									+ session.getAttributes().get("serverPort")
									+ session.getAttributes().get("serverContextPath") + ApiConfig.USER_PATH
									+ ApiConfig.DISPLAY_USER_AVATAR + "/"
									+ user.getAvatar().substring(0, user.getAvatar().indexOf(".")));
			final UserRoom userRoom = null;
			userLiveRoom.forEach(ur -> {
				if (ur.getRoomId().equals(roomId)) {
					ur = userRoom;
				}
			});
			Viewer viewer = new Viewer(userInfo, user.getRoles().toArray(new String[0]), roomId);
			room.get(userRoom).put(String.valueOf(user.getId()), viewer);
		}
		return message;
	}

	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (StompCommand.UNSUBSCRIBE.equals(accessor.getCommand())) {
			UserDetails user = (UserDetails) session.getPrincipal();
			String roomId = (String) accessor.getHeader("id");
			final UserRoom userRoom = null;
			userLiveRoom.forEach(ur -> {
				if (ur.getRoomId().equals(roomId)) {
					ur = userRoom;
				}
			});
			Map<String, Viewer> viewers = room.get(userRoom);
			viewers.remove(String.valueOf(user.getUserId()));
			if (room.get(userRoom).isEmpty()) {
				room.remove(userRoom);
				userLiveRoom.remove(userRoom);
			}
		}
	}
}
