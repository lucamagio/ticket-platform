package it.lessons.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import it.lessons.ticket_platform.repository.CategorieRepository;
import it.lessons.ticket_platform.repository.NoteRepository;
import it.lessons.ticket_platform.repository.TicketRepository;
import it.lessons.ticket_platform.repository.TicketStatusRepository;
import it.lessons.ticket_platform.repository.UserRepository;
import it.lessons.ticket_platform.service.TicketService;
import it.lessons.ticket_platform.model.Ticket;
import it.lessons.ticket_platform.model.TicketStatus;
import it.lessons.ticket_platform.model.Note;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    //Creazione nota
    @PostMapping("/editNote")
    public String editNote(@Valid @ModelAttribute("nota") Note formNota, BindingResult bindingResult, Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", false);
            model.addAttribute("nota", formNota);

            return "note/editNote";
        }

        noteRepository.save(formNota);
        
        return "redirect:/admin/dettaglioTicket" + formNota.getTicket().getId();
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
    public String doEditNote(@Valid @ModelAttribute("nota") Note formNota, BindingResult bindingResult, Model model) {
       
        if (bindingResult.hasErrors()) {

            model.addAttribute("editMode", true);
            model.addAttribute("nota", formNota);

            return "note/editNote";
        }

        noteRepository.save(formNota);
        
        return "redirect:/admin/dettaglioTicket" + formNota.getTicket().getId();
    }
    
}
