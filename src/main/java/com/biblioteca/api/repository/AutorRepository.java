package com.biblioteca.api.repository;

import com.biblioteca.api.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data JPA para a entidade {@link Autor}.
 *
 * <p>Atualmente usa apenas os métodos CRUD herdados de {@link JpaRepository}
 * (findAll, findById, save, delete, findAllById). Está separado para manter
 * a simetria com os demais recursos da API e permitir futuras consultas.
 */
@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
}
