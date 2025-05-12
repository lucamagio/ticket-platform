package it.lessons.ticket_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.lessons.ticket_platform.Repository.TicketRepository;
import it.lessons.ticket_platform.model.Ticket;

@Service
public class TicketService {

    @Autowired
    public TicketRepository ticketRepository;

    public List<Ticket> filtroTitolo(String titolo){

        if (titolo != null && !titolo.isBlank()) {
            return ticketRepository.findByTitoloContainingIgnoreCase(titolo);
        } else{
            return ticketRepository.findAll();
        }
    }

    public Optional<Ticket> ticketSpecificoId(Long id){

        try {

            return ticketRepository.findById(id);

        } catch (IllegalArgumentException e) {

            System.err.println("Errore durante la lettura del ticket con id null" + e);
            
            return null;
        }
        

    }

}
