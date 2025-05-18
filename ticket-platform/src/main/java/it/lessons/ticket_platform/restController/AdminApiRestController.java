package it.lessons.ticket_platform.restController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.lessons.ticket_platform.model.Ticket;
import it.lessons.ticket_platform.service.TicketService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/admin")
public class AdminApiRestController {

    @Autowired
    private TicketService ticketService;

    @GetMapping()
    public ResponseEntity<List<Ticket>> findTicketByTitolo(@RequestParam(name = "keyword", required = false) String param) {

        List<Ticket> ticket = ticketService.filtroTitolo(param);

        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Ticket>> findTicketByCategoria(@PathVariable("categoriaId") Integer categoriaId) {

        List<Ticket> ticket = ticketService.filtroCategoria(categoriaId);


        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }
    
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<Ticket>> findTicketByStatus(@PathVariable("statusId") Integer statusId) {

        List<Ticket> ticket = ticketService.filtroStatus(statusId);
        
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }
    
}
