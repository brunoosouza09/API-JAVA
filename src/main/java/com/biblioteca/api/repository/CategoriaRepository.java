package com.biblioteca.api.repository;

import com.biblioteca.api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data JPA para a entidade {@link Categoria}.
 *
 * <p>Estende {@link JpaRepository} para herdar os métodos CRUD padrão
 * e expõe a consulta derivada usada na verificação de nome duplicado.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    /**
     * Indica se já existe uma categoria com o mesmo nome (case-insensitive).
     *
     * @param nome nome a verificar
     * @return {@code true} se já houver categoria com esse nome
     */
    boolean existsByNomeIgnoreCase(String nome);
}
