package com.system.spring.exception.process;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.system.spring.exception.JwtMissingException;
import com.system.spring.exception.JwtTokenMalformedException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Aspect
@Component
public class JwtExceptionProcess {

	static Logger log = Logger.getLogger(JwtExceptionProcess.class.getName());

	@Around("execution(* com.system.spring.utils.JwtUtil.validateToken(..))")
	public void processJwtException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		try {
			proceedingJoinPoint.proceed();
		} catch (SignatureException e) {
			throw new JwtTokenMalformedException("Invalid JWT signature");
		} catch (MalformedJwtException e) {
			throw new JwtTokenMalformedException("Invalid JWT token");
		} catch (ExpiredJwtException e) {
			throw new JwtTokenMalformedException("Expired JWT token");
		} catch (UnsupportedJwtException e) {
			throw new JwtTokenMalformedException("Unsupported JWT token");
		} catch (IllegalArgumentException e) {
			throw new JwtMissingException("JWT claims string is empty.");
		}
	}
}
