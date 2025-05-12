package it.lessons.ticket_platform.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.lessons.ticket_platform.Repository.TicketRepository;
import it.lessons.ticket_platform.model.Ticket;
import it.lessons.ticket_platform.service.TicketService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    public  TicketRepository ticketRepository;

    @Autowired
    public  TicketService ticketService;

    @GetMapping()
    public String list(Model model, @RequestParam(name="keyword", required = false) String titolo) {

        model.addAttribute("list", ticketService.filtroTitolo(titolo));
        
        return "admin/indexAdmin";
    }

    @GetMapping("/dettaglioTicket/{id}")
    public String dettaglioTicket(@PathVariable ("id") Long id, Model model) {
        Optional<Ticket> optTicket = ticketService.ticketSpecificoId(id);

        if (optTicket == null) {
            model.addAttribute("errorMessage", "Errore: id non valido");
            return "Error/idError";
        }
        if (optTicket.isPresent()) {
            model.addAttribute("ticket", optTicket.get());
            return "ticket/dettaglioTicket";
        }

        model.addAttribute("errorCause", "Non esiste un ticket con id " + id);
        model.addAttribute("errorMessage","Errore di ricerca del ticket");

        return "error/genericError";
    }
    

}
