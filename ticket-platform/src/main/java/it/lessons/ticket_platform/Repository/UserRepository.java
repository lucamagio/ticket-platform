package it.lessons.ticket_platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.lessons.ticket_platform.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    public Optional<User> findByEmail(String email);

    @Query("SELECT user FROM User user JOIN user.roles role WHERE role.name = 'OPERATORE' AND user.disponibile = true")
    List<User> findOperatoriDisponibili();
}
