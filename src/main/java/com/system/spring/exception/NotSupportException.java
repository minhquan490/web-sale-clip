package com.system.spring.exception;

@SuppressWarnings("serial")
public class NotSupportException extends RuntimeException {

	public NotSupportException(String msg) {
		super(msg);
	}
}
