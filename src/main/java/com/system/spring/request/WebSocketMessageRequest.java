package com.system.spring.request;

public class WebSocketMessageRequest {

	public enum MessageType {
		LIVE, CHAT, LEAVE, JOIN
	}

	private Object content;
	private String sender;
	private MessageType type;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

}
