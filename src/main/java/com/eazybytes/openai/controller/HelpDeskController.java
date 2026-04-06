package com.eazybytes.openai.controller;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybytes.openai.tool.HelpDeskTool;

@RestController
@RequestMapping("/api/tools")
public class HelpDeskController {

	private final ChatClient helpDeskChatClient;
	private final HelpDeskTool helpDeskTool;
	
	public HelpDeskController(@Qualifier("helpDeskChatClient")ChatClient helpDeskChatClient, HelpDeskTool helpDeskTool) {
		this.helpDeskChatClient = helpDeskChatClient;
		this.helpDeskTool = helpDeskTool;
	}
	
	@GetMapping("/help-desk")
	public ResponseEntity<String> helpDesk(@RequestParam String username, @RequestParam String message) {
		String answer = helpDeskChatClient.prompt()
				.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, username))
				.user(message)
				.tools(helpDeskTool)
				.toolContext(Map.of("username", username))
				.call()
				.content();
		return ResponseEntity.ok(answer);
	}
	
}
