package com.system.spring.exception.handler;

import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.spring.exception.MissingFieldException;
import com.system.spring.exception.ResourceNotFoundException;
import com.system.spring.response.ServerResponse;

@Aspect
@ControllerAdvice
public class GlobalExceptionHandler {

	static Logger log = Logger.getLogger(GlobalExceptionHandler.class.getName());

	@AfterThrowing(pointcut = "execution(* com.system.spring.*.*(..))", throwing = "e")
	@ExceptionHandler(Throwable.class)
	public @ResponseBody ResponseEntity<ServerResponse> ExceptionHandler(Throwable e) {
		log.error(e.getMessage(), e);
		e.printStackTrace();
		return new ResponseEntity<ServerResponse>(
				new ServerResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Unknown error"),
				HttpStatus.BAD_REQUEST);
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.*.*(..))", throwing = "e")
	@ExceptionHandler(ResourceNotFoundException.class)
	public @ResponseBody ResponseEntity<ServerResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
		log.warn(e.getMessage(), e);
		return new ResponseEntity<ServerResponse>(
				new ServerResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@AfterThrowing(pointcut = "execution(* com.system.spring.*.*(..))", throwing = "e")
	@ExceptionHandler(MissingFieldException.class)
	public @ResponseBody ResponseEntity<ServerResponse> missingFieldExceptionHandler(MissingFieldException e) {
		log.error(e.getMessage(), e);
		return new ResponseEntity<ServerResponse>(
				new ServerResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, e.getMessage()),
				HttpStatus.BAD_REQUEST);
	}
}
