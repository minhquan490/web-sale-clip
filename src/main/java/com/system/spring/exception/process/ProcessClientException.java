package com.system.spring.exception.process;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.fileupload.FileUploadException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import com.system.spring.exception.InvalidUserCredentialsException;
import com.system.spring.exception.NotSupportException;
import com.system.spring.exception.ResourceNotFoundException;
import com.system.spring.exception.UnknowException;
import com.system.spring.utils.CauseUtil;

@Aspect
@Component
public class ProcessClientException {

	@Autowired
	private CauseUtil message;

	@Around("execution(* com.system.spring.controller..*.*(..))")
	public Object processException(ProceedingJoinPoint point) throws Throwable {
		try {
			Object object = point.proceed();
			return object;
		} catch (NullPointerException e) {
			MethodSignature signature = (MethodSignature) point.getSignature();
			String cause = message.getMessageNullPointerCause(signature.getMethod().getName());
			throw new ResourceNotFoundException(cause, e);
		} catch (BadCredentialsException e) {
			throw new InvalidUserCredentialsException("Username or password is invalid !", e);
		} catch (NotSupportException e) {
			throw new NotSupportException("Your format type is not supported !", e);
		} catch (FileUploadException e) {
			throw new FileUploadException("Error when upload your file, please try again !");
		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (IOException | ServletException e) {
			throw new UnknowException("Unknow error", e);
		}
	}
}
