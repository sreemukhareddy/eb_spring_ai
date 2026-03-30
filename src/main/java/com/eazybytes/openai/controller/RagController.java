package com.eazybytes.openai.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rag")
public class RagController {

	private final ChatClient chatClient;
	private final VectorStore vectorStore;
	
	@Value("classpath:/promptTemplates/systemPromptRandomDataTemplate.st")
	Resource promptTemplate;
	
	@Value("classpath:/promptTemplates/hrPromptRagTemplate.st")
	Resource hrSystemTemplate;
	
	private final ChatClient webSearchRAGChatClient;

	public RagController(@Qualifier("chatMemoryChatClient") ChatClient chatClient, VectorStore vectorStore, @Qualifier("webSearchRAGChatClient") ChatClient webSearchRAGChatClient) {
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;
		this.webSearchRAGChatClient = webSearchRAGChatClient;
	}
	
	@GetMapping("/random/chat")
	public ResponseEntity<?> chat(@RequestParam("message")String message, @RequestParam("username")String username) {
		
		SearchRequest searchRequest = SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5)
		.build();
		
		List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);
		
		String similarContext = similarDocs.stream()
				   .map(Document::getText)
				   .collect(Collectors.joining(System.lineSeparator()));
		
		String answer = chatClient.prompt().system(promptSystemSpec -> {
			promptSystemSpec.text(promptTemplate)
			.param("documents", similarContext);
		})
		.advisors(advisorSpec -> {
					advisorSpec.param(ChatMemory.CONVERSATION_ID, username);
				})
		.user(message)
		.call()
		.content();
		
		return ResponseEntity.ok(answer);
	}
	
	@GetMapping("/document/chat")
	public ResponseEntity<?> document(@RequestParam("message")String message, @RequestParam("username")String username) {
		
		/*
		SearchRequest searchRequest = SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5)
		.build();
		
		List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);
		
		String similarContext = similarDocs.stream()
				   .map(Document::getText)
				   .collect(Collectors.joining(System.lineSeparator()));
		*/
		
		String answer = chatClient.prompt()
				/*.system(promptSystemSpec -> {
					promptSystemSpec.text(hrSystemTemplate)
									.param("documents", similarContext);
				})*/
		.advisors(advisorSpec -> {
					advisorSpec.param(ChatMemory.CONVERSATION_ID, username);
				})
		.user(message)
		.call()
		.content();
		
		return ResponseEntity.ok(answer);
	}
	
	@GetMapping("/web-search/chat")
	public ResponseEntity<?> webSearchChat(@RequestParam("message")String message, @RequestParam("username")String username) {
		
		
		String answer = webSearchRAGChatClient.prompt()
		.advisors(advisorSpec -> {
					advisorSpec.param(ChatMemory.CONVERSATION_ID, username);
				})
		.user(message)
		.call()
		.content();
		
		return ResponseEntity.ok(answer);
	}
}
