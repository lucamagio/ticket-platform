package it.lessons.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.ticket_platform.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
