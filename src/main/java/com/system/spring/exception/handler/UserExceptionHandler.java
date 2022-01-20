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
import com.system.spring.exception.MissingFieldException;
import com.system.spring.exception.ResourceNotFoundException;

@Aspect
@Component
public class UserExceptionHandler {

	static Logger log = Logger.getLogger(UserExceptionHandler.class.getName());

	@Around("execution(* com.system.spring.controller.HomeController.login(..))")
	public void processUserControllerException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		try {
			proceedingJoinPoint.proceed();
		} catch (DisabledException e) {
			throw new DisabledUserException("User Inactive");
		} catch (BadCredentialsException e) {
			throw new InvalidUserCredentialsException("Invalid Credentials");
		}
	}

	@Around("execution(* com.system.spring.controller.client.UserController.getMyClipPurchased())")
	public void processUserClipPurchasedException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		try {
			proceedingJoinPoint.proceed();
		} catch (NullPointerException e) {
			throw new ResourceNotFoundException("No clip is purchased");
		}
	}

	@Around("execution(* com.system.spring.controller.client.UserController.getMyClips())")
	public void processMyClips(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		try {
			proceedingJoinPoint.proceed();
		} catch (NullPointerException e) {
			throw new ResourceNotFoundException("You not upload any clip");
		}
	}

	@Around("execution(* com.system.spring.controller.client.UserController.updateMyInfo(..))")
	public void processUpdateMyInfo(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		try {
			proceedingJoinPoint.proceed();
		} catch (NullPointerException e) {
			throw new MissingFieldException("Please insert missing field");
		}
	}
}
