package com.system.spring.exception.handler;

import java.time.LocalDateTime;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.spring.response.ServerResponse;
import com.system.spring.utils.HttpStatusUtil;

@Aspect
@ControllerAdvice
public class HandlerClientException {

	@Autowired
	private HttpStatusUtil status;

	@AfterThrowing(pointcut = "execution(* com.system.spring.exception.process.ProcessExceptionClient.*.*(..))", throwing = "e")
	@ExceptionHandler(Throwable.class)
	public @ResponseBody ResponseEntity<ServerResponse> handleException(Exception e) {

		int beginIndex = e.getClass().getName().lastIndexOf(".") + 1;
		int endIndex = e.getClass().getName().length();
		HttpStatus httpstatus = status.getStatus(e.getClass().getName().substring(beginIndex, endIndex));
		return ResponseEntity.status(httpstatus)
				.body(new ServerResponse(LocalDateTime.now(), httpstatus, e.getMessage()));

	}
}