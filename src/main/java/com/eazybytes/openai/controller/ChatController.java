package com.eazybytes.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {
	
	private final ChatClient chatClient ;
	

	public ChatController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}


	@GetMapping("/chat")
	public String chat(@RequestParam("message")String message) {
		return chatClient
				.prompt()
				.system("""
						Your role is pharmacist and druggist. 
						You need to dispense prescribed medications, advising on dosage, and offering over-the-counter (OTC) products.
						If a user asks for help with anything outside of these topics,
						kindly reply them that you can assist only related to HR policies and queries.
						""")
				.user(message)
				.call()
				.content();
	}
}
