package it.lessons.ticket_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.lessons.ticket_platform.repository.CategorieRepository;
import it.lessons.ticket_platform.repository.NoteRepository;
import it.lessons.ticket_platform.repository.TicketRepository;
import it.lessons.ticket_platform.repository.TicketStatusRepository;
import it.lessons.ticket_platform.repository.UserRepository;
import it.lessons.ticket_platform.model.Categoria;
import it.lessons.ticket_platform.model.Note;
import it.lessons.ticket_platform.model.Ticket;
import it.lessons.ticket_platform.model.User;

@Service
public class TicketService {

    @Autowired
    public TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private TicketStatusRepository ticketStatusRepository;

    @Autowired
    private NoteRepository noteRepository;

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

    public void ticketDelete(Long id){

        Ticket ticket = ticketRepository.findById(id).get();

        for(Note nota : ticket.getNote()){
            noteRepository.deleteById(nota.getId());
        }
    }

}