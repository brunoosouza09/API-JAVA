package com.biblioteca.api.repository;

import com.biblioteca.api.model.Editora;
import com.biblioteca.api.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data JPA para a entidade {@link Livro}.
 *
 * <p>Além dos métodos CRUD herdados de {@link JpaRepository}, expõe
 * consultas derivadas usadas pelos services para garantir regras de
 * negócio (ISBN único e exclusão de editora com livros vinculados).
 */
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    /**
     * Indica se já existe um livro cadastrado com o ISBN informado.
     *
     * @param isbn ISBN a verificar (13 dígitos)
     * @return {@code true} se já existir livro com esse ISBN
     */
    boolean existsByIsbn(String isbn);

    /**
     * Indica se há ao menos um livro vinculado à editora informada.
     * Usado para impedir a exclusão de editoras com livros.
     *
     * @param editora editora a consultar
     * @return {@code true} se houver livros para a editora
     */
    boolean existsByEditora(Editora editora);
}
