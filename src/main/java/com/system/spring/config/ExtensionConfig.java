package com.system.spring.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExtensionConfig {

	@Bean(name = "extensionMap")
	Map<String, String> getExtension() {
		Map<String, String> extensionMap = new HashMap<String, String>();

		extensionMap.put("mpeg", ".mpa");
		extensionMap.put("mp4", ".mp4");
		extensionMap.put("x-msvideo", ".avi");

		extensionMap.put("bmp", ".bmp");
		extensionMap.put("png", ".png");
		extensionMap.put("jpeg", ".jpg");
		extensionMap.put("svg+xml", ".svg");
		return extensionMap;
	}
}
