package com.system.spring.exception;

@SuppressWarnings("serial")
public class WrongPasswordUsernameException extends RuntimeException {

	public WrongPasswordUsernameException(String msg) {
		super(msg);
	}
}
