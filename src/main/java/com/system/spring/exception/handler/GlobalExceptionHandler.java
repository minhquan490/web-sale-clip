package com.system.spring.exception.handler;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.system.spring.exception.DisabledUserException;
import com.system.spring.exception.InvalidUserCredentialsException;
import com.system.spring.exception.JwtMissingException;
import com.system.spring.exception.JwtTokenMalformedException;
import com.system.spring.exception.ResourceNotFoundException;

@Aspect
@Component
public class GlobalExceptionHandler {

	static Logger log = Logger.getLogger(GlobalExceptionHandler.class.getName());

	@AfterThrowing(pointcut = "execution(* com.system.spring.*.*(..))", throwing = "e")
	public ResponseEntity<String> ExceptionHandler(Exception e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.badRequest().body("Unknown error");
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.*.*(..))", throwing = "e")
	public ResponseEntity<String> ResourceNotFoundExceptionHandler(ResourceNotFoundException e) {
		log.warn(e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.handler.*.*(..))", throwing = "e")
	public ResponseEntity<String> jwtExceptionHandler(JwtTokenMalformedException e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.handler.*.*(..))", throwing = "e")
	public ResponseEntity<String> jwtMissingExceptionHandler(JwtMissingException e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.handler.*.*(..))", throwing = "e")
	public ResponseEntity<String> userDisabledExceptionHandler(DisabledUserException e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.handler.*.*(..))", throwing = "e")
	public ResponseEntity<String> invalidUserCredentialsExceptionHandler(InvalidUserCredentialsException e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
