package com.system.spring.utils;

import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CauseUtil {

	@Autowired
	private BeanFactory context;

	private Map<String, String> getNullPointerMap() {
		@SuppressWarnings("unchecked")
		Map<String, String> cause = (Map<String, String>) context.getBean("causeOfNullPointer");
		return cause;
	}

	public String getMessageNullPointerCause(String cause) {
		Map<String, String> causeMap = getNullPointerMap();
		return causeMap.get(cause);
	}
}
