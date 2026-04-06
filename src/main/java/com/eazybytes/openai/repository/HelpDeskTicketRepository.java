package com.eazybytes.openai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eazybytes.openai.entity.HelpDeskTicket;

@Repository
public interface HelpDeskTicketRepository extends JpaRepository<HelpDeskTicket, Long> {
	List<HelpDeskTicket> findByUsername(String username);
}
