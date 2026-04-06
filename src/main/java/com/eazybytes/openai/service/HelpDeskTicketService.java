package com.eazybytes.openai.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eazybytes.openai.entity.HelpDeskTicket;
import com.eazybytes.openai.model.TicketRequest;
import com.eazybytes.openai.repository.HelpDeskTicketRepository;

@Service
public class HelpDeskTicketService {

	private final HelpDeskTicketRepository helpDeskTicketRepository;

	public HelpDeskTicketService(HelpDeskTicketRepository helpDeskTicketRepository) {
		
		this.helpDeskTicketRepository = helpDeskTicketRepository;
	}
	
	public HelpDeskTicket createTicket(TicketRequest ticketInput, String username) {
		HelpDeskTicket ticket = new HelpDeskTicket();
		ticket.setIssue(ticketInput.issue());
		ticket.setUsername(username);
		ticket.setStatus("OPEN");
		ticket.setCreatedAt(LocalDateTime.now());
		ticket.setEta(LocalDateTime.now().plusDays(7));
        return helpDeskTicketRepository.save(ticket);
    }

    public List<HelpDeskTicket> getTicketsByUsername(String username) {
        return helpDeskTicketRepository.findByUsername(username);
    }
	
}
