package com.system.spring.response;

import org.springframework.http.HttpStatus;

public class ServerFilterResponse {

	private String timeResponse;
	private HttpStatus code;
	private String message;

	public ServerFilterResponse(String timeResponse, HttpStatus code, String message) {
		super();
		this.timeResponse = timeResponse;
		this.code = code;
		this.message = message;
	}

	public String getTimeResponse() {
		return timeResponse;
	}

	public void setTimeResponse(String timeResponse) {
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
