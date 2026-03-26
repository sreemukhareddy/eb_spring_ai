package com.eazybytes.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eazybytes.openai.advisors.TokenUsageAuditAdvisor;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
		
		ChatOptions chatOptions = ChatOptions.builder().model("gpt-4.1-mini").temperature(0.8).build();
		
		return chatClientBuilder
		.defaultSystem("""
				You are an internal HR assistant. Your role is to help employees with questions related to HR policies,
				such as	leave polocies, working hours, benifits, and code of conduct.
				If a user asks for help with anything outside of these topics,
				kindly reply them that you can assist only related to HR policies and queries.
				""")
		.defaultAdvisors(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor())
		.defaultOptions(chatOptions)
		.build();
	}
	
	@Bean
	public ChatMemory chatMemory(JdbcChatMemoryRepository chatMemoryRepository) {
		return MessageWindowChatMemory.builder().maxMessages(10)
		.chatMemoryRepository(chatMemoryRepository)
		.build();
	}
	
	@Bean
	public ChatClient chatMemoryChatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
		Advisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
		return chatClientBuilder
				.defaultAdvisors(new SimpleLoggerAdvisor(), messageChatMemoryAdvisor)
				.build();
	}
}
