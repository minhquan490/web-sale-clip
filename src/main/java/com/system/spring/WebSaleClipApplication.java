package com.system.spring;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.system.spring.websocket.entity.UserRoom;
import com.system.spring.websocket.entity.Viewer;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = { "com.system.spring", "controller" })
public class WebSaleClipApplication {

	@Bean(name = "userLiveRooms")
	public Set<UserRoom> userLiveRooms() {
		return new HashSet<>();
	}

	@Bean(name = "room")
	public Map<UserRoom, Map<String, Viewer>> room() {
		return new HashMap<>();
	}

	public static void main(String[] args) {
		SpringApplication.run(WebSaleClipApplication.class, args);
	}

}
