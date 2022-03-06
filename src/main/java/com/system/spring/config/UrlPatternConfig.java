package com.system.spring.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UrlPatternConfig {

	@Bean
	public String[] urlPattern() {
		Set<String> urlPatern = new HashSet<>();
		urlPatern.add(ApiConfig.HOME_PATH);
		urlPatern.add(ApiConfig.LOGIN_PATH);
		urlPatern.add(ApiConfig.REGISTER_PATH);
		urlPatern.add(ApiConfig.CLIP_PATH + ApiConfig.PLAY + "/*");
		return urlPatern.toArray(new String[0]);
	}
}
