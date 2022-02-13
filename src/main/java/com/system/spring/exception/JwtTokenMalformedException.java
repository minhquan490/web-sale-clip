package com.system.spring.exception;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class JwtTokenMalformedException extends AuthenticationException {

	public JwtTokenMalformedException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
