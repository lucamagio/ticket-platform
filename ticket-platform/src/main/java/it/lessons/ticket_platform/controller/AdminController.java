package it.lessons.ticket_platform.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.lessons.ticket_platform.repository.CategorieRepository;
import it.lessons.ticket_platform.repository.TicketRepository;
import it.lessons.ticket_platform.repository.TicketStatusRepository;
import it.lessons.ticket_platform.repository.UserRepository;
import it.lessons.ticket_platform.model.Ticket;
import it.lessons.ticket_platform.model.TicketStatus;
import it.lessons.ticket_platform.service.TicketService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private  TicketRepository ticketRepository;

    @Autowired
    private  TicketService ticketService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private TicketStatusRepository ticketStatusRepository;

    @GetMapping()
    public String list(Model model, @RequestParam(name="keyword", required = false) String titolo) {

        model.addAttribute("list", ticketService.filtroTitolo(titolo));
        
        return "admin/indexAdmin";
    }

    //Dettaglio ticket
    @GetMapping("/dettaglioTicket/{id}")
    public String dettaglioTicket(@PathVariable ("id") Long id, Model model) {
        Optional<Ticket> optTicket = ticketService.ticketSpecificoId(id);

        if (optTicket.isPresent()) {
            model.addAttribute("ticket", optTicket.get());
            return "ticket/dettaglioTicket";
        }

        model.addAttribute("errorCause", "Non esiste un ticket con id " + id);
        model.addAttribute("errorMessage","Errore di ricerca del ticket");

        return "error/genericError";
    }
    
    //Creazione ticket
    @GetMapping("/createTicket")
    public String createTicket(Model model) {
        Ticket ticket = new Ticket();

        TicketStatus status = ticketStatusRepository.findById(1).get(); //Impostazione di default dello status del ticket

        ticket.setStatus(status);

        model.addAttribute("ticket", new Ticket());
        model.addAttribute("listaUser", userRepository.findAll());
        model.addAttribute("listaCategorie", categorieRepository.findAll());
        return "ticket/createTicket";
    }

    //Modifica ticket
    @GetMapping("/editTicket/{id}")
    public String editTicket(@PathVariable ("id") Long id, Model model) {

        model.addAttribute("listaUser", userRepository.findAll());
        model.addAttribute("listaCategorie", categorieRepository.findAll());
        model.addAttribute("listaStatus", ticketStatusRepository.findAll());
        model.addAttribute("ticket", ticketRepository.findById(id).get());

        return "ticket/editTicket";
    }

    //Cancellazione ticket
    @PostMapping("/delete/{id}")
    public String deleteTicket(@PathVariable ("id") Long id) {
        
        ticketRepository.deleteById(id);
        
        return "redirect:/admin";
    }
    

    
    

}
