package com.system.spring.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ServerResponse {

	private LocalDateTime timeResponse;
	private HttpStatus code;
	private Object message;

	public ServerResponse(LocalDateTime timeResponse, HttpStatus code, Object message) {
		super();
		this.timeResponse = timeResponse;
		this.code = code;
		this.message = message;
	}

	public LocalDateTime getTimeResponse() {
		return timeResponse;
	}

	public void setTimeResponse(LocalDateTime timeResponse) {
		this.timeResponse = timeResponse;
	}

	public HttpStatus getCode() {
		return code;
	}

	public void setCode(HttpStatus code) {
		this.code = code;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}
}