package com.eazybytes.openai.tool;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.eazybytes.openai.entity.HelpDeskTicket;
import com.eazybytes.openai.model.TicketRequest;
import com.eazybytes.openai.service.HelpDeskTicketService;

@Component
public class HelpDeskTool {

	private final HelpDeskTicketService ticketService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HelpDeskTool.class);

	public HelpDeskTool(HelpDeskTicketService ticketService) {
		this.ticketService = ticketService;
	}
	
	@Tool(name = "createTicket", description = "Create the Support Ticket", returnDirect = true)
	String createTicket(@ToolParam(required = true, description = "Details to create a support ticket") TicketRequest ticketRequest, 
			ToolContext toolContext
			) {
		String username = (String) toolContext.getContext().get("username");
        LOGGER.info("Creating support ticket for user: {} with details: {}", username, ticketRequest);
        HelpDeskTicket savedTicket = ticketService.createTicket(ticketRequest,username);
        LOGGER.info("Ticket created successfully. Ticket ID: {}, Username: {}", savedTicket.getId(), savedTicket.getUsername());
        return "Ticket #" + savedTicket.getId() + " created successfully for user " + savedTicket.getUsername();
	}
	
	@Tool(description = "Fetch the status of the tickets based on a given username")
    public List<HelpDeskTicket> getTicketStatus(ToolContext toolContext) {
        String username = (String) toolContext.getContext().get("username");
        LOGGER.info("Fetching tickets for user: {}", username);
        List<HelpDeskTicket> tickets =  ticketService.getTicketsByUsername(username);
        LOGGER.info("Found {} tickets for user: {}", tickets.size(), username);
        return tickets;
    }
	
}
