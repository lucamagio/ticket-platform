package it.lessons.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import it.lessons.ticket_platform.repository.CategorieRepository;
import it.lessons.ticket_platform.repository.TicketRepository;
import it.lessons.ticket_platform.repository.TicketStatusRepository;
import it.lessons.ticket_platform.repository.UserRepository;
import it.lessons.ticket_platform.model.Ticket;
import it.lessons.ticket_platform.model.TicketStatus;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketStatusRepository ticketStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    //Creazione ticket
    @PostMapping("/createTicket")
    public String createTicket(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {

            return "ticket/createTicket";
        }
        if (formTicket.getStatus() == null) {
            TicketStatus status = ticketStatusRepository.findById(1).get();

            formTicket.setStatus(status);
        }

        ticketRepository.save(formTicket);
        redirectAttributes.addFlashAttribute("succesCreate", "Ticket Creato!");
        
        return "redirect:/admin";
    }

    //Modifica ticket
    @PostMapping("/editTicket/{id}")
    public String editTicket(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaUser", userRepository.findAll());
            model.addAttribute("listaCategorie", categorieRepository.findAll());
            model.addAttribute("listaStatus", ticketStatusRepository.findAll());
            return "ticket/editTicket";
        }

        ticketRepository.save(formTicket);
        redirectAttributes.addFlashAttribute("succesEdit", "Ticket Modificato!");

        return "redirect:/admin";
    }
    
}
