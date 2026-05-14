package com.biblioteca.api.repository;

import com.biblioteca.api.model.Editora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditoraRepository extends JpaRepository<Editora, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}
