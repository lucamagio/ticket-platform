package it.lessons.ticket_platform.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.lessons.ticket_platform.repository.CategorieRepository;
import it.lessons.ticket_platform.repository.NoteRepository;
import it.lessons.ticket_platform.repository.TicketRepository;
import it.lessons.ticket_platform.repository.TicketStatusRepository;
import it.lessons.ticket_platform.repository.UserRepository;
import it.lessons.ticket_platform.security.DatabaseUserDetails;
import it.lessons.ticket_platform.model.Ticket;
import it.lessons.ticket_platform.model.User;
import it.lessons.ticket_platform.service.TicketService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/user")
public class UserController {

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

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping()
    public String list(Model model, @RequestParam(name="keyword", required = false) String titolo) {

        model.addAttribute("list", ticketService.filtroTitoloPerOperatoreLoggato(titolo));
        
        return "user/indexUser";
    }

    //Dettaglio profilo
    @GetMapping("/profiloUser/{id}")
    public String profiloUser(@PathVariable ("id") Integer id, Model model) {
        
        Optional<User> optUser = userRepository.findById(id);

        if (optUser.isPresent()) {
            model.addAttribute("user", optUser.get());
            return "user/profiloUser";
        }

        model.addAttribute("errorCause", "Non esiste uno User con id " + id);
        model.addAttribute("errorMessage","Errore di ricerca dello User");

        return "error/genericError";
    }

    //Modifica profilo
    @GetMapping("/editProfilo/{id}")
    public String editProfilo(@PathVariable ("id") Integer id, Model model) {

        model.addAttribute("user", userRepository.findById(id).get());

        return "user/editProfilo";
    }

    @PostMapping("/editProfilo/{id}")
    public String editProfilo(@Valid @ModelAttribute("user") User formUser, BindingResult bindingResult, Model model) {
        
        int ticketAperti = ticketRepository.countTicketApertiByUserId(formUser.getId());
        if (!formUser.getDisponibile() && ticketAperti > 0) {
            formUser.setDisponibile(true);
            model.addAttribute("user", formUser);
            model.addAttribute("erroreDisponibilita", "Non puoi disattivarti se hai ticket aperti");
            return "user/editProfilo";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", formUser);
            return "user/editProfilo";
        }

        //Risettaggio del ruolo nello user
        User existingUser = userRepository.findById(formUser.getId()).get();
        formUser.setRoles(existingUser.getRoles());

        userRepository.save(formUser);

        //Aggiornamento delle autorizzazioni dell'utente dopo la modifica del profilo
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getName().equals(formUser.getEmail())) {

        UserDetails updatedUserDetails = new DatabaseUserDetails(formUser);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
            updatedUserDetails, 
            updatedUserDetails.getPassword(), 
            updatedUserDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
        
        return "user/profiloUser";
    }
    
    //Dettaglio ticket
    @GetMapping("/dettaglioTicket/{id}")
    public String dettaglioTicket(@PathVariable ("id") Long id, Model model) {
        Optional<Ticket> optTicket = ticketService.ticketSpecificoId(id);

        if (optTicket.isPresent()) {
            model.addAttribute("ticket", optTicket.get());
            model.addAttribute("listaNote", noteRepository.findAll());
            return "ticket/dettaglioTicket";
        }

        model.addAttribute("errorCause", "Non esiste un ticket con id " + id);
        model.addAttribute("errorMessage","Errore di ricerca del ticket");

        return "error/genericError";
    }

    //Modifica stato ticket
    @GetMapping("editTicketStatus/{id}")
    public String editTicketStatus(@PathVariable("id") Long id, Model model) {

        model.addAttribute("ticket", ticketRepository.findById(id).get());
        model.addAttribute("listaStatus", ticketStatusRepository.findAll());
        model.addAttribute("listaUser", userRepository.findAll());
        model.addAttribute("listaCategorie", categorieRepository.findAll());

        return "ticket/editTicketStatus";
    }
    
    
}
