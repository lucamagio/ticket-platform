package it.lessons.ticket_platform.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.ticket_platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>{

    List<Ticket> findByTitoloContainingIgnoreCase(String titolo);
}
