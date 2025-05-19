package it.lessons.ticket_platform.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import it.lessons.ticket_platform.repository.NoteRepository;
import it.lessons.ticket_platform.repository.UserRepository;
import it.lessons.ticket_platform.security.DatabaseUserDetails;
import it.lessons.ticket_platform.model.Note;
import it.lessons.ticket_platform.model.User;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    //Creazione nota
    @PostMapping("/editNote")
    public String editNote(@Valid @ModelAttribute("nota") Note formNota, BindingResult bindingResult, Model model, @AuthenticationPrincipal DatabaseUserDetails userDetails) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", false);
            model.addAttribute("nota", formNota);

            return "note/editNote";
        }

        Optional<User> optUser = userRepository.findById(userDetails.getId());
        if (optUser.isPresent()) {
            User user = optUser.get();
            formNota.setUser(user);
        }

        noteRepository.save(formNota);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return "redirect:/admin/dettaglioTicket/" + formNota.getTicket().getId();
        } else {
            return "redirect:/user/dettaglioTicket/" + formNota.getTicket().getId();
        }
    }
    
    //Modifica nota
    @GetMapping("/editNote/{id}")
    public String editNote(@PathVariable("id") Long id, Model model) {

        Note nota = noteRepository.findById(id).get();

        model.addAttribute("editMode", true);
        model.addAttribute("nota", nota);

        return "note/editNote";
    }

    @PostMapping("/editNote/{id}")
    public String doEditNote(@Valid @ModelAttribute("nota") Note formNota, BindingResult bindingResult, Model model, @AuthenticationPrincipal DatabaseUserDetails userDetails) {
       
        if (bindingResult.hasErrors()) {

            model.addAttribute("editMode", true);
            model.addAttribute("nota", formNota);

            return "note/editNote";
        }

        Optional<User> optUser = userRepository.findById(userDetails.getId());

        if (optUser.isPresent()) {
            User user = optUser.get();
            formNota.setUser(user);
        }

        noteRepository.save(formNota);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return "redirect:/admin/dettaglioTicket/" + formNota.getTicket().getId();
        } else {
            return "redirect:/user/dettaglioTicket/" + formNota.getTicket().getId();
        }
    }
    
}
