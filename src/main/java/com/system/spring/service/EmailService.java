package com.system.spring.service;

public interface EmailService {

	void sendEmail(String to, String subject, String body);
}
