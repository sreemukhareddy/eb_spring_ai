package com.eazybytes.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eazybytes.openai.advisors.TokenUsageAuditAdvisor;
import com.eazybytes.openai.tool.TimeTools;

@Configuration
public class TimeChatClientConfig {
	
	@Bean
	public ChatClient timeChatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, TimeTools timeTools) {
		Advisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
		Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();
		return chatClientBuilder
				.defaultAdvisors(new SimpleLoggerAdvisor(), messageChatMemoryAdvisor, tokenUsageAdvisor)
				.defaultTools(timeTools)
				.build();
	}
}
