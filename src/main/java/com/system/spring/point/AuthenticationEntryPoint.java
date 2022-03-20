package com.system.spring.point;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.spring.response.ServerFilterResponse;
import com.system.spring.utils.HttpStatusUtil;

@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

	@Autowired
	private HttpStatusUtil status;

	protected String convertObjToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		int beginIndex = authException.getCause().getClass().getName().lastIndexOf(".") + 1;
		int endIndex = authException.getCause().getClass().getName().length();
		HttpStatus httpstatus = status
				.getStatus(authException.getCause().getClass().getName().substring(beginIndex, endIndex));
		ServerFilterResponse serverResponse = new ServerFilterResponse(LocalDateTime.now().toString(), httpstatus,
				authException.getMessage());
		HttpServletResponse resp = response;
		resp.setStatus(httpstatus.value());
		resp.getWriter().write(convertObjToJson(serverResponse));
	}
}