package com.system.spring.config.websocket;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.system.spring.authentication.WebSocketAuthenticator;
import com.system.spring.exception.handler.HandlerWebSocketConfigException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class SecDefaultHandshakeHandler extends DefaultHandshakeHandler {

	@Autowired
	private WebSocketAuthenticator authenticator;

	@Autowired
	private HandlerWebSocketConfigException handlerWebSocketConfigException;

	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		String token = (String) attributes.get("token");
		if (SecurityContextHolder.getContext().getAuthentication() == null
				|| !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			try {
				SecurityContextHolder.getContext().setAuthentication(authenticator.getAuthenticationOrFail(token));
			} catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException
					| AuthenticationException | IllegalAccessException e) {
				handlerWebSocketConfigException.handlerWebSocketConfigException(e);
			}
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (Principal) auth.getPrincipal();
	}
}
