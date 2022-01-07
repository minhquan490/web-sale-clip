package com.system.spring.exception;

@SuppressWarnings("serial")
public class DisabledUserException extends RuntimeException {

	public DisabledUserException(String message) {
		super(message);
	}
}
