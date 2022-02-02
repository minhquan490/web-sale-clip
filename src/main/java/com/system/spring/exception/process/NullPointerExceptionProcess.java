package com.system.spring.exception.process;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.spring.exception.ResourceNotFoundException;
import com.system.spring.utils.CauseUtil;

@Aspect
@Component
public class NullPointerExceptionProcess {

	static Logger log = Logger.getLogger(NullPointerExceptionProcess.class.getName());

	@Autowired
	private CauseUtil message;

	@Around("execution(* com.system.spring.controller.client.*.*(..))")
	public void processNullExceptionAtMyClipPurchased(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		try {
			proceedingJoinPoint.proceed();
		} catch (NullPointerException e) {
			MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
			String casue = message.getMessageNullPointerCause(signature.getMethod().getName());
			throw new ResourceNotFoundException(casue);
		}
	}

}
