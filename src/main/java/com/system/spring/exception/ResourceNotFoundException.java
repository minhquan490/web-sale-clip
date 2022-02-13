package com.system.spring.exception;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException() {

	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}