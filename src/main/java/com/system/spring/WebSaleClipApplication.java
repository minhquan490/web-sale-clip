package com.system.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class WebSaleClipApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSaleClipApplication.class, args);
	}

}
