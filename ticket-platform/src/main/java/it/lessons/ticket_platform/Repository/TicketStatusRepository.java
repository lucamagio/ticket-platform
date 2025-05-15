package it.lessons.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.ticket_platform.model.TicketStatus;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Integer>{

}
