package com.system.spring.config.websocket.interceptors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.system.spring.config.websocket.authencation.WebSocketAuthenticator;
import com.system.spring.details.UserDetails;
import com.system.spring.exception.handler.HandlerWebSocketConfigException;
import com.system.spring.response.UserInfo;
import com.system.spring.websocket.entity.Viewer;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

	private static final String TOKEN_HEADER = "Authorization";
	private final WebSocketAuthenticator authenticator;

	@Autowired
	private HandlerWebSocketConfigException handler;

	@Autowired
	@Qualifier("userConnected")
	private ConcurrentHashMap<String, Viewer> userConnected;

	@Autowired
	public AuthChannelInterceptorAdapter(final WebSocketAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (accessor.getCommand().equals(StompCommand.CONNECT)) {
			Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
			final String token = (String) sessionAttributes.get(TOKEN_HEADER);
			try {
				final UsernamePasswordAuthenticationToken user = authenticator.getAuthenticationOrFail(token);
				accessor.setUser(user);
				UserDetails userDetails = (UserDetails) user.getPrincipal();
				UserInfo userInfo = new UserInfo(userDetails.getUserId(), userDetails.getUser().getFirstName(),
						userDetails.getUser().getLastName(), userDetails.getUser().getEmail(),
						userDetails.getUser().getGender(), userDetails.getUser().getBirthDate().toString(),
						userDetails.getUsername(), userDetails.getUser().getAvatar());
				String[] roles = userDetails.getUser().getRoles().toArray(new String[0]);
				userConnected.putIfAbsent(accessor.getSessionId(), new Viewer(userInfo, roles, null));
			} catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException
					| AuthenticationException | IllegalAccessException e) {
				handler.handlerWebSocketConfigException(e);
			}
		}
		return message;
	}
}
