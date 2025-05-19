package it.lessons.ticket_platform.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.lessons.ticket_platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>{

    List<Ticket> findByTitoloContainingIgnoreCase(String titolo);
    List<Ticket> findByUserId(Integer userId);
    
    // :userId è un segnaposto che verrà sostituito del @Param passato dal metodo
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.user.id = :userId AND t.status.id IN (1, 2)")
    int countTicketApertiByUserId(@Param("userId") Integer userId);

    @Query("SELECT t FROM Ticket t JOIN t.categorie c WHERE c.id = :categoriaId")
    List<Ticket> findByCategoriaId(@Param("categoriaId") Integer categoriaId);

    @Query("SELECT t FROM Ticket t JOIN t.status s WHERE s.id = :statusId")
    List<Ticket> findByStatusId(@Param("statusId") Integer statusId);

    @Query("SELECT t FROM Ticket t WHERE t.user.id = :userId AND LOWER(t.titolo) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Ticket> findByUserIdAndTitoloContainingIgnoreCase(@Param("userId") Integer userId, @Param("keyword") String keyword);


}
