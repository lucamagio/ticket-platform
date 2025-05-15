package it.lessons.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.ticket_platform.model.Categoria;

public interface CategorieRepository extends JpaRepository<Categoria, Integer> {

}
