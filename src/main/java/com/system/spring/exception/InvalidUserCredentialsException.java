package com.system.spring.exception;

@SuppressWarnings("serial")
public class InvalidUserCredentialsException extends RuntimeException {

	public InvalidUserCredentialsException(String message) {
		super(message);
	}

}
