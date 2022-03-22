package com.system.spring.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.system.spring.config.ApiConfig;

@Configuration
@EnableWebSocket
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketConfigurer {

	@Bean
	public WebSocketHandler handler() {
		return new WebSocketHandler();
	}

	@Bean
	public WebSocketHandshake handshake() {
		return new WebSocketHandshake();
	}

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxBinaryMessageBufferSize(100 * 1024 * 1024);
		container.setMaxTextMessageBufferSize(10 * 1024 * 1024);
		return container;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(handler(), ApiConfig.WATCH_PATH).addInterceptors(handshake()).withSockJS();
	}

}
