package com.system.spring.exception;

@SuppressWarnings("serial")
public class MissingFieldException extends RuntimeException {

	public MissingFieldException(String message) {
		super(message);
	}
}
