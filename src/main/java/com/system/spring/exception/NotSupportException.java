package com.system.spring.exception;

@SuppressWarnings("serial")
public class NotSupportException extends RuntimeException {

	public NotSupportException() {

	}

	public NotSupportException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
