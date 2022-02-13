package com.system.spring.exception;

@SuppressWarnings("serial")
public class UnknowException extends RuntimeException {

	public UnknowException() {
		super();
	}

	public UnknowException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
