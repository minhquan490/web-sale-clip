package com.system.spring.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class StatusConfig {

	@Bean(name = "httpStatusMap")
	public Map<String, HttpStatus> getStatus() {
		Map<String, HttpStatus> statusMap = new HashMap<>();

		statusMap.put("ExpiredJwtException", HttpStatus.UNAUTHORIZED);
		statusMap.put("SignatureException", HttpStatus.UNAUTHORIZED);
		statusMap.put("MalformedJwtException", HttpStatus.BAD_REQUEST);
		statusMap.put("UnsupportedJwtException", HttpStatus.BAD_REQUEST);
		statusMap.put("IllegalArgumentException", HttpStatus.UNAUTHORIZED);
		statusMap.put("IllegalAccessException", HttpStatus.BAD_REQUEST);
		statusMap.put("ResourceNotFoundException", HttpStatus.NOT_FOUND);
		statusMap.put("InvalidUserCredentialsException", HttpStatus.UNAUTHORIZED);
		statusMap.put("NotSupportException", HttpStatus.NOT_ACCEPTABLE);
		statusMap.put("FileUploadException", HttpStatus.BAD_REQUEST);
		statusMap.put("UnknowException", HttpStatus.INTERNAL_SERVER_ERROR);
		return statusMap;
	}
}
