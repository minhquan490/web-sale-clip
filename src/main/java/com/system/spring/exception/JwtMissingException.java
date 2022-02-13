package com.system.spring.exception;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class JwtMissingException extends AuthenticationException {

	public JwtMissingException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
