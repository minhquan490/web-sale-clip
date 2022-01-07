package com.system.spring.exception.handler;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Component;

import com.system.spring.exception.DisabledUserException;
import com.system.spring.exception.InvalidUserCredentialsException;

@Aspect
@Component
public class UserExceptionHandler {

	static Logger log = Logger.getLogger(UserExceptionHandler.class.getName());

	@Around("execution(* com.system.spring.controller.UserController.login(..))")
	public void processUserControllerException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		try {
			proceedingJoinPoint.proceed();
		} catch (DisabledException e) {
			throw new DisabledUserException("User Inactive");
		} catch (BadCredentialsException e) {
			throw new InvalidUserCredentialsException("Invalid Credentials");
		}
	}
}
