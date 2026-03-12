package com.eazybytes.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybytes.openai.advisors.TokenUsageAuditAdvisor;

@RestController
@RequestMapping("/api")
public class PromptTemplateController {

	private final ChatClient chatClient;
	
	public PromptTemplateController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}
	
	@Value("classpath:/promptTemplates/systemPromptTemplate.st")
	Resource systemTemplatePrompt;
	
	@GetMapping("/prompt-stuffing")
	public String chat( @RequestParam("message")String message) {
		return chatClient
				.prompt()
				.system(systemTemplatePrompt)
				//.advisors(new TokenUsageAuditAdvisor())
				.user(message)
				.call()
				.content();
	}
}
