package com.system.spring.config.websocket;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		attributes.put("token", request.getHeaders().get("Authorization"));
		attributes.putIfAbsent("serverScheme", req.getScheme());
		attributes.putIfAbsent("serverName", req.getServerName());
		attributes.putIfAbsent("serverPort", String.valueOf(req.getServerPort()));
		attributes.putIfAbsent("serverContextPath", req.getContextPath());
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// use default method
	}

}
