package com.eazybytes.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptStuffingController {

	private final ChatClient chatClient;
	
	public PromptStuffingController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}
	
	String prompt = """
			A customer named {customerName} sent the following message:
			"${customerMessage}"
			
			Write a polite and helpful email response addressing the issue.
			Maintain a professional tone and provide reassurance.
			
			Respond as if you're writing the email body only. Dont include subject, signature.
			""";
	
	@Value("classpath:/promptTemplates/userPromptTemplate.st")
	Resource userTemplatePrompt;
	
	@GetMapping("/email")
	public String chat(@RequestParam("customerName")String customerName, @RequestParam("customerMessage")String customerMessage) {
		return chatClient
				.prompt()
				.system("""
						You are professional customer service assistant which helps in drafting mail responses to improve the productivity of the customer support team.
						If a user asks for help with anything outside of these topics,
						kindly reply them that you can assist only related to HR policies and queries.
						""")
				.user(promptTemplateSpec -> {
					promptTemplateSpec.text(userTemplatePrompt)
					.param("customerName", customerName)
					.param("customerMessage", customerMessage);
				})
				.call()
				.content();
	}
}
