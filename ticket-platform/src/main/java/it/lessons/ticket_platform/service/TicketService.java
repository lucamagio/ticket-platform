package it.lessons.ticket_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import it.lessons.ticket_platform.repository.CategorieRepository;
import it.lessons.ticket_platform.repository.NoteRepository;
import it.lessons.ticket_platform.repository.TicketRepository;
import it.lessons.ticket_platform.repository.TicketStatusRepository;
import it.lessons.ticket_platform.repository.UserRepository;
import it.lessons.ticket_platform.security.DatabaseUserDetails;
import it.lessons.ticket_platform.model.Note;
import it.lessons.ticket_platform.model.Ticket;
import it.lessons.ticket_platform.model.User;
import it.lessons.ticket_platform.model.Categoria;
import it.lessons.ticket_platform.model.TicketStatus;

@Service
public class TicketService {

    @Autowired
    public TicketRepository ticketRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private TicketStatusRepository ticketStatusRepository;


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

    public List<Ticket> getTicketPerOperatoreLoggato() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DatabaseUserDetails userDetails = (DatabaseUserDetails) auth.getPrincipal();

        Integer userId = userDetails.getId();

        // Recupera i ticket associati all'id dell'operatore loggato
        return ticketRepository.findByUserId(userId.intValue());
    }

    public void isDisponibile(Integer userId){

        int ticketAperti = ticketRepository.countTicketApertiByUserId(userId);

        User user = userRepository.findById(userId).get();

        user.setDisponibile(ticketAperti == 0);

        userRepository.save(user);
    }

    public List<Ticket> filtroCategoria(Integer categoriaId){

        Optional<Categoria> optCategoria = categorieRepository.findById(categoriaId);

        if (optCategoria.isEmpty()) {
            return ticketRepository.findAll();
        } else{
            return ticketRepository.findByCategoriaId(categoriaId);
        }
    }

    public List<Ticket> filtroStatus(Integer statusId){
        
        Optional<TicketStatus> optStatus = ticketStatusRepository.findById(statusId);

        if (optStatus.isEmpty()) {
            return ticketRepository.findAll();
        } else{
            return ticketRepository.findByStatusId(statusId);
        }
    }

    public List<Ticket> filtroTitoloPerOperatoreLoggato(String titolo) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DatabaseUserDetails userDetails = (DatabaseUserDetails) auth.getPrincipal();
        Integer userId = userDetails.getId();
    
        if (titolo != null && !titolo.isBlank()) {
            return ticketRepository.findByUserIdAndTitoloContainingIgnoreCase(userId, titolo);
        } else {
            return ticketRepository.findByUserId(userId);
        }
    }

}