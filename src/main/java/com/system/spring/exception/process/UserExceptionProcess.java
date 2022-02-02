package com.system.spring.exception.process;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Component;

import com.system.spring.exception.DisabledUserException;
import com.system.spring.exception.WrongPasswordUsernameException;

@Aspect
@Component
public class UserExceptionProcess {

	@Around("execution(* com.system.spring.controller.LoginController.login(..))") // Logic bug
	public void processUserException(ProceedingJoinPoint point) throws Throwable {
		try {
			point.proceed();
		} catch (BadCredentialsException e) {
			throw new WrongPasswordUsernameException("Username or password is wrong !");
		} catch (DisabledException e) {
			throw new DisabledUserException("Your account is disabled. Contact with support if you need");
		}
	}
}
