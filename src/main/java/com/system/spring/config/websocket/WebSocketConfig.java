package com.system.spring.config.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
	private HttpHandshakeInterceptor httpHandshakeInterceptor;

	@Autowired
	private SecDefaultHandshakeHandler secDefaultHandshakeHandler;

	@Autowired
	private JwtChannelInterceptor jwtChannelInterceptor;

	@Autowired
	private WebSocketDecorate webSocketDecorate;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").setHandshakeHandler(secDefaultHandshakeHandler)
				.addInterceptors(httpHandshakeInterceptor).withSockJS().setSessionCookieNeeded(false);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(1);
		threadPoolTaskScheduler.setThreadNamePrefix("heart-beat");
		threadPoolTaskScheduler.initialize();

		registry.setPathMatcher(new AntPathMatcher("."));
		registry.enableSimpleBroker("/topic").setTaskScheduler(threadPoolTaskScheduler)
				.setHeartbeatValue(new long[] { 4000, 4000 });
		registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(jwtChannelInterceptor);
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		registry.addDecoratorFactory(webSocketDecorate);
	}
}
