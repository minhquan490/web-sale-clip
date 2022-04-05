package com.system.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class RoomController {

	private SimpMessagingTemplate template;

	@Autowired
	public RoomController(SimpMessagingTemplate template) {
		this.template = template;
	}

}