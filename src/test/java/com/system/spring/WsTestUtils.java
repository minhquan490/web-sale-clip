package com.system.spring;

import java.lang.reflect.Type;
import java.util.function.Consumer;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class WsTestUtils {

	protected WebSocketStompClient createSocketStompClient() {
		WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
		stompClient.setMessageConverter(new StringMessageConverter());
		return stompClient;
	}

	static class StompSessionHandler extends StompSessionHandlerAdapter {
		@Override
		public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
			super.afterConnected(session, connectedHeaders);
		}

		@Override
		public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
				Throwable exception) {
			super.handleException(session, command, headers, payload, exception);
		}
	}

	public static class StompFrameHandler implements org.springframework.messaging.simp.stomp.StompFrameHandler {

		private final Consumer<String> frameHandler;

		public StompFrameHandler(Consumer<String> frameHandler) {
			this.frameHandler = frameHandler;
		}

		@Override
		public Type getPayloadType(StompHeaders headers) {
			return String.class;
		}

		@Override
		public void handleFrame(StompHeaders headers, Object payload) {
			frameHandler.accept(payload.toString());
		}

	}
}
