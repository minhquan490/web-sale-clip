package com.system.spring.utils;

import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExtensionFile {

	@Autowired
	private BeanFactory context;

	@SuppressWarnings("unchecked")
	private Map<String, String> getExtensionMap() {
		return (Map<String, String>) context.getBean("extensionMap");
	}

	public String generateExtension(String contentType) {
		Map<String, String> extensionMap = getExtensionMap();
		int begin = contentType.indexOf("/");
		String extensionName = contentType.substring(begin + 1);
		return extensionMap.get(extensionName);
	}
}
