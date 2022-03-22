package com.system.spring.config.websocket;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.system.spring.authentication.WebSocketAuthenticator;
import com.system.spring.exception.handler.HandlerWebSocketConfigException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class WebSocketHandshake implements HandshakeInterceptor {

	@Autowired
	private WebSocketAuthenticator authenticator;

	@Autowired
	private HandlerWebSocketConfigException handlerException;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			final String TOKEN = req.getHeader("Authorization");
			attributes.computeIfAbsent(TOKEN, getUserFromToken -> {
				Authentication auth;
				try {
					auth = authenticator.getAuthenticationOrFail(TOKEN);
					return auth.getPrincipal();
				} catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException
						| AuthenticationException | IllegalAccessException e) {
					handlerException.handlerWebSocketConfigException(e);
					return null;
				}
			});
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// TODO Auto-generated method stub

	}

}
