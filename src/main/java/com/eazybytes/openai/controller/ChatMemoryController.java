package com.eazybytes.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatMemoryController {
	
	private final ChatClient chatClient ;
	

	public ChatMemoryController(@Qualifier("chatMemoryChatClient") ChatClient chatClient) {
		this.chatClient = chatClient;
	}


	@GetMapping("/chat-memory")
	public ResponseEntity<?> chat(@RequestParam("message")String message, @RequestParam("username")String username) {
		return ResponseEntity.ok(
				chatClient
				.prompt()
				.advisors(advisorSpec -> {
					advisorSpec.param(ChatMemory.CONVERSATION_ID, username);
				})
				.user(message).call().content());
	}
}
