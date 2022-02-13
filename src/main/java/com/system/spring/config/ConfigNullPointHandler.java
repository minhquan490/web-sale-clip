package com.system.spring.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigNullPointHandler {

	@Bean(name = "causeOfNullPointer")
	public Map<String, String> getCause() {
		Map<String, String> cause = new HashMap<String, String>();
		cause.put("getMyClipPurchased", "You have not purchased any products yet");
		cause.put("getMyClips", "You have not uploaded any content yet");
		cause.put("updateMyInfo",
				"Missing field. Please checking your infomation to update following fomat ('First name', 'Last name', 'Gender', 'Birth date')");
		cause.put("register",
				"Missing field. Please checking your infomation to update following fomat ('First name', 'Last name', 'Gender', 'Birth date')");
		return cause;
	}
}
