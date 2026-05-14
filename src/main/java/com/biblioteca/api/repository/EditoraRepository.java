package com.biblioteca.api.repository;

import com.biblioteca.api.model.Editora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data JPA para a entidade {@link Editora}.
 *
 * <p>Já oferece os métodos CRUD herdados de {@link JpaRepository}
 * (findAll, findById, save, delete, etc.). Aqui adicionamos apenas o
 * derivado de consulta usado pelas validações de duplicidade do service.
 */
@Repository
public interface EditoraRepository extends JpaRepository<Editora, Long> {
    /**
     * Indica se já existe uma editora com o mesmo nome (case-insensitive).
     *
     * @param nome nome a verificar
     * @return {@code true} se já houver editora com esse nome
     */
    boolean existsByNomeIgnoreCase(String nome);
}
