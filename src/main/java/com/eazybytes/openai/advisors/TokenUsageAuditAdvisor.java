package com.eazybytes.openai.advisors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;

public class TokenUsageAuditAdvisor implements CallAdvisor {
	
	private final Logger log = LoggerFactory.getLogger(TokenUsageAuditAdvisor.class);

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
		ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
		try {
			Usage usage = chatClientResponse.chatResponse().getMetadata().getUsage();
			Integer totalTokens = usage.getTotalTokens();
			log.info("Token usage is " + totalTokens);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chatClientResponse;
	}

}
