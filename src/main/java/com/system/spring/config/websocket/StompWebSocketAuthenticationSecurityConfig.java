package com.system.spring.config.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.system.spring.config.websocket.interceptors.AuthChannelInterceptorAdapter;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompWebSocketAuthenticationSecurityConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
	private AuthChannelInterceptorAdapter authChannelInterceptorAdapter;

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(authChannelInterceptorAdapter);
	}
}
