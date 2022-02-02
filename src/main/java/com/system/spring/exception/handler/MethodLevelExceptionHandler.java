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

import com.system.spring.exception.NotSupportException;
import com.system.spring.response.ServerResponse;

@Aspect
@ControllerAdvice
@Order(1)
public class MethodLevelExceptionHandler {

	Logger log = Logger.getLogger(MethodLevelExceptionHandler.class.getName());

	@AfterThrowing(pointcut = "execution(* com.system.spring.controller.client.ResourceUploadController.*(..))", throwing = "e")
	@ExceptionHandler(NotSupportException.class)
	public @ResponseBody ResponseEntity<ServerResponse> notSupportExceptionHandler(NotSupportException e) {
		log.error(e.getMessage(), e);
		return new ResponseEntity<ServerResponse>(
				new ServerResponse(LocalDateTime.now(), HttpStatus.NOT_IMPLEMENTED, e.getMessage()),
				HttpStatus.NOT_IMPLEMENTED);
	}
}
