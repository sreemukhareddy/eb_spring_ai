package com.eazybytes.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.eazybytes.openai.advisors.TokenUsageAuditAdvisor;
import com.eazybytes.openai.tool.TimeTools;

@Configuration
public class HelpDeskChatClientConfig {
	
	@Value("classpath:/promptTemplates/helpDeskSystemPromptTemplate.st")
	Resource promptTemplate;
	
	@Bean
	public ChatClient helpDeskChatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, TimeTools timeTools) {
		Advisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
		Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();
		return chatClientBuilder
				.defaultSystem(promptTemplate)
				.defaultAdvisors(new SimpleLoggerAdvisor(), messageChatMemoryAdvisor, tokenUsageAdvisor)
				.defaultTools(timeTools)
				.build();
	}
}
