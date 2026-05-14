// Pacote dos repositórios.
package com.biblioteca.api.repository;

// Entidade da qual este repositório cuida.
import com.biblioteca.api.model.Editora;
// JpaRepository já oferece os métodos CRUD básicos.
import org.springframework.data.jpa.repository.JpaRepository;
// @Repository marca a interface como bean de acesso a dados.
import org.springframework.stereotype.Repository;

/*
 * Repositório de Editora.
 * Estende JpaRepository<Entidade, TipoDoId> para ganhar
 * findAll, findById, save, delete, count, etc.
 */
@Repository
public interface EditoraRepository extends JpaRepository<Editora, Long> {
    // Verifica se já existe editora com este nome (ignorando maiúsculas/minúsculas).
    // Spring Data gera automaticamente: SELECT 1 FROM editora WHERE LOWER(nome) = LOWER(?)
    boolean existsByNomeIgnoreCase(String nome);
}
