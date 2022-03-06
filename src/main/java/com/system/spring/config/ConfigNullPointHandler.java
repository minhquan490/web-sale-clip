package com.system.spring.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigNullPointHandler {

	@Bean(name = "causeOfNullPointer")
	public Map<String, String> getCause() {
		Map<String, String> cause = new HashMap<>();
		cause.put("getMyClipPurchased", "You have not purchased any products yet");
		cause.put("getMyClips", "You have not uploaded any content yet");
		cause.put("updateMyInfo",
				"Missing field. Please checking your infomation to update following format ('First name', 'Last name', 'Gender', 'Birth date')");
		cause.put("register",
				"Missing field. Please checking your infomation to update following format ('First name', 'Last name', 'Gender', 'Birth date')");
		cause.put("createInfoClip",
				"Missing field. Please checking your clip infomation following format ('Clip name', 'Price', 'Categories')");
		cause.put("createCategory", "Missing field. Please checking your name of category");
		cause.put("createCreditInfo",
				"Missing field. Please checking your credit infomation following format ('Credit Number', 'Pass Code', 'type', 'Date Expire')");
		cause.put("createCategory", "Missing category name");
		return cause;
	}
}
