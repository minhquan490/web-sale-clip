package com.system.spring.controller;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.spring.config.ApiConfig;
import com.system.spring.response.ServerResponse;
import com.system.spring.websocket.entity.UserRoom;
import com.system.spring.websocket.entity.Viewer;

@Controller
public class RoomController {

	@Autowired
	@Qualifier("rooms")
	private ConcurrentHashMap<UserRoom, ConcurrentHashMap<String, Viewer>> rooms;

	private SimpMessagingTemplate template;

	@Autowired
	public RoomController(SimpMessagingTemplate template) {
		this.template = template;
	}

	public void sendMessageToSubcrible(String path, String payload) {
		this.template.convertAndSend(path, payload);
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'actor', 'admin')")
	@GetMapping(path = ApiConfig.WATCH_PATH)
	public @ResponseBody ResponseEntity<ServerResponse> listWatch() {
		Set<UserRoom> actorIsLiveStream = rooms.keySet();
		if (actorIsLiveStream.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, "No actor is streaming"));
		} else {
			return ResponseEntity.ok(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, actorIsLiveStream));
		}
	}
}