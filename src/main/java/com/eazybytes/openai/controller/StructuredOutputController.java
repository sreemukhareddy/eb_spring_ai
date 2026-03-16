package com.eazybytes.openai.controller;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybytes.openai.model.CountryCities;

@RestController
@RequestMapping("/api")
public class StructuredOutputController {
	
	private final ChatClient chatClient ;
	

	public StructuredOutputController(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
	}


	@GetMapping("/chat-bean")
	public ResponseEntity<CountryCities> chat(@RequestParam("message")String message) {
		return ResponseEntity.ok(chatClient
				.prompt()
				.options(OpenAiChatOptions.builder()
						.model(ChatModel.GPT_5_MINI).build()
						)
				.user(message)
				.call()
				//.entity(new BeanOutputConverter<>(CountryCities.class)));
				.entity(CountryCities.class));
	}
	
	@GetMapping("/chat-list")
	public ResponseEntity<List<String>> list(@RequestParam("message")String message) {
		return ResponseEntity.ok(chatClient
				.prompt()
				.options(OpenAiChatOptions.builder()
						.model(ChatModel.GPT_5_MINI).build()
						)
				.user(message)
				.call()
				.entity(new ListOutputConverter()));
	}
	
	@GetMapping("/chat-map")
	public ResponseEntity<Map<String, Object>> map(@RequestParam("message")String message) {
		return ResponseEntity.ok(chatClient
				.prompt()
				.options(OpenAiChatOptions.builder()
						.model(ChatModel.GPT_5_MINI).build()
						)
				.user(message)
				.call()
				.entity(new MapOutputConverter()));
	}
	
	@GetMapping("/chat-bean-list")
	public ResponseEntity<List<CountryCities>> chatBeanlist(@RequestParam("message")String message) {
		return ResponseEntity.ok(chatClient
				.prompt()
				.options(OpenAiChatOptions.builder()
						.model(ChatModel.GPT_5_MINI).build()
						)
				.user(message)
				.call()
				.entity(new ParameterizedTypeReference<List<CountryCities>>() {
				}));
	}
}
