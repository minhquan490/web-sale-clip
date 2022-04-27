package com.system.spring.utils;

import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class HttpStatusUtil {

	@Autowired
	private BeanFactory context;

	@SuppressWarnings("unchecked")
	private Map<String, HttpStatus> getHttpStatusMap() {
		return (Map<String, HttpStatus>) context.getBean("httpStatusMap");
	}

	public HttpStatus getStatus(String cause) {
		Map<String, HttpStatus> httpStatusMap = getHttpStatusMap();
		return httpStatusMap.get(cause);
	}
}
