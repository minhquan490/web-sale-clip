package com.system.spring.exception;

import org.springframework.security.authentication.DisabledException;

@SuppressWarnings("serial")
public class DisabledUserException extends DisabledException {

	public DisabledUserException(String message, Throwable cause) {
		super(message, cause);
	}
}
