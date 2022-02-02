package com.system.spring.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

@SuppressWarnings("unused")
public class ServerResponse {

	private LocalDateTime timeResponse;
	private HttpStatus code;
	private String message;

	public ServerResponse(LocalDateTime timeResponse, HttpStatus code, String message) {
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}