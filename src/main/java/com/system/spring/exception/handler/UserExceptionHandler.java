package com.system.spring.exception.handler;

import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.spring.exception.DisabledUserException;
import com.system.spring.exception.InvalidUserCredentialsException;
import com.system.spring.exception.JwtMissingException;
import com.system.spring.exception.JwtTokenMalformedException;
import com.system.spring.exception.WrongPasswordUsernameException;
import com.system.spring.response.ServerResponse;

@Aspect
@ControllerAdvice
@Order(0)
public class UserExceptionHandler {

	static Logger log = Logger.getLogger(UserExceptionHandler.class.getName());

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.process.*.*(..))", throwing = "e")
	@ExceptionHandler(JwtTokenMalformedException.class)
	public @ResponseBody ResponseEntity<ServerResponse> jwtExceptionHandler(JwtTokenMalformedException e) {
		log.error(e.getMessage(), e);
		return new ResponseEntity<ServerResponse>(
				new ServerResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.process.*.*(..))", throwing = "e")
	@ExceptionHandler(JwtMissingException.class)
	public @ResponseBody ResponseEntity<ServerResponse> jwtMissingExceptionHandler(JwtMissingException e) {
		log.error(e.getMessage(), e);
		return new ResponseEntity<ServerResponse>(
				new ServerResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.process.*.*(..))", throwing = "e")
	@ExceptionHandler(InvalidUserCredentialsException.class)
	public @ResponseBody ResponseEntity<ServerResponse> invalidUserCredentialsExceptionHandler(
			InvalidUserCredentialsException e) {
		log.error(e.getMessage(), e);
		return new ResponseEntity<ServerResponse>(
				new ServerResponse(LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE, e.getMessage()),
				HttpStatus.NOT_ACCEPTABLE);
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.process.UserExceptionProcess.processUserException(..))", throwing = "e")
	@ExceptionHandler(WrongPasswordUsernameException.class)
	public @ResponseBody ResponseEntity<ServerResponse> badCredentialsUser(WrongPasswordUsernameException e) {
		log.info(e.getMessage());
		return new ResponseEntity<ServerResponse>(
				new ServerResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.process.UserExceptionProcess.processUserException(..))", throwing = "e")
	@ExceptionHandler(DisabledUserException.class)
	public @ResponseBody ResponseEntity<ServerResponse> disabledUser(DisabledUserException e) {
		log.info(e.getMessage());
		return new ResponseEntity<ServerResponse>(
				new ServerResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}
}
