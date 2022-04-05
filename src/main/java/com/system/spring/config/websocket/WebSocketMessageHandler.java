package com.system.spring.config.websocket;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * A modify class of {@link AbstractWebSocketHandler} for handling
 * {@link PingMessage}
 */

public abstract class WebSocketMessageHandler extends AbstractWebSocketHandler {

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		if (message instanceof TextMessage) {
			handleTextMessage(session, (TextMessage) message);
		} else if (message instanceof BinaryMessage) {
			handleBinaryMessage(session, (BinaryMessage) message);
		} else if (message instanceof PongMessage) {
			handlePongMessage(session, (PongMessage) message);
		} else if (message instanceof PingMessage) {
			handlePingMessage(session, (PingMessage) message);
		} else {
			throw new IllegalStateException("Unexpected WebSocket message type: " + message);
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
	}

	protected void handlePingMessage(WebSocketSession session, PingMessage message) throws Exception {

	}
}
