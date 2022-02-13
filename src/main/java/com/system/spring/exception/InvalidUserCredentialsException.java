package com.system.spring.exception;

@SuppressWarnings("serial")
public class InvalidUserCredentialsException extends RuntimeException {

	public InvalidUserCredentialsException() {
		super();
	}

	public InvalidUserCredentialsException(String message, Throwable cause) {
		super(message, cause);
	}

}
