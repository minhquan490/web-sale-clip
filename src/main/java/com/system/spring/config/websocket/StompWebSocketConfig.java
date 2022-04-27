package com.system.spring.config.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;

import com.system.spring.websocket.entity.UserRoom;
import com.system.spring.websocket.entity.Viewer;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Bean("rooms")
	public ConcurrentHashMap<UserRoom, ConcurrentHashMap<String /* session id of user */, Viewer>> rooms() {
		return new ConcurrentHashMap<>();
	}

	@Bean("userConnected")
	public ConcurrentHashMap<String /* user session id is a key */, Viewer> userConnected() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public HandshakeInterceptor httpHandshakeInterceptor() {
		return new HandshakeInterceptor() {

			@Override
			public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
					WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
				if (request instanceof ServletServerHttpRequest) {
					ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
					HttpServletRequest servletRequest = serverHttpRequest.getServletRequest();
					Cookie token = WebUtils.getCookie(servletRequest, "Authorization");
					attributes.put("Authorization", token.getValue());
				}
				return true;
			}

			@Override
			public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
					WebSocketHandler wsHandler, Exception exception) {
				// Auto-generated method stub

			}
		};
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").withSockJS().setHeartbeatTime(15000).setInterceptors(httpHandshakeInterceptor());
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic", "/queue");
		registry.setApplicationDestinationPrefixes("/app");
	}
}
