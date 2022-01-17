package com.system.spring.exception.handler;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.spring.exception.DisabledUserException;
import com.system.spring.exception.InvalidUserCredentialsException;
import com.system.spring.exception.JwtMissingException;
import com.system.spring.exception.JwtTokenMalformedException;
import com.system.spring.exception.MissingFieldException;
import com.system.spring.exception.ResourceNotFoundException;

@Aspect
@ControllerAdvice
public class GlobalExceptionHandler {

	static Logger log = Logger.getLogger(GlobalExceptionHandler.class.getName());

	@AfterThrowing(pointcut = "execution(* com.system.spring.*.*(..))", throwing = "e")
	@ExceptionHandler(Exception.class)
	public @ResponseBody ResponseEntity<String> ExceptionHandler(Exception e) {
		log.error(e.getMessage(), e);
		e.printStackTrace();
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.*.*(..))", throwing = "e")
	@ExceptionHandler(ResourceNotFoundException.class)
	public @ResponseBody ResponseEntity<String> ResourceNotFoundExceptionHandler(ResourceNotFoundException e) {
		log.warn(e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.handler.*.*(..))", throwing = "e")
	@ExceptionHandler(JwtTokenMalformedException.class)
	public @ResponseBody ResponseEntity<String> jwtExceptionHandler(JwtTokenMalformedException e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.handler.*.*(..))", throwing = "e")
	@ExceptionHandler(JwtMissingException.class)
	public @ResponseBody ResponseEntity<String> jwtMissingExceptionHandler(JwtMissingException e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.handler.*.*(..))", throwing = "e")
	@ExceptionHandler(DisabledUserException.class)
	public @ResponseBody ResponseEntity<String> userDisabledExceptionHandler(DisabledUserException e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.handler.*.*(..))", throwing = "e")
	@ExceptionHandler(InvalidUserCredentialsException.class)
	public @ResponseBody ResponseEntity<String> invalidUserCredentialsExceptionHandler(
			InvalidUserCredentialsException e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.*.*(..))", throwing = "e")
	@ExceptionHandler(MissingFieldException.class)
	public @ResponseBody ResponseEntity<String> missingFieldExceptionHandler(MissingFieldException e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
