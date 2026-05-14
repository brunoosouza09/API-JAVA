// Pacote dos repositórios (acesso a dados via Spring Data JPA).
package com.biblioteca.api.repository;

// Entidades usadas nos métodos do repositório.
import com.biblioteca.api.model.Editora;
import com.biblioteca.api.model.Livro;
// JpaRepository já oferece CRUD pronto (findAll, findById, save, delete, etc.).
import org.springframework.data.jpa.repository.JpaRepository;
// @Repository marca a interface como componente de acesso a dados.
import org.springframework.stereotype.Repository;

/*
 * Repositório de Livro.
 * Herda os métodos CRUD do JpaRepository e adiciona alguns métodos
 * derivados (criados a partir do nome) para checagens de regra de negócio.
 */
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    // Método derivado: o Spring Data gera o SQL automaticamente baseado no nome.
    // existsByIsbn -> SELECT 1 FROM livro WHERE isbn = ?
    boolean existsByIsbn(String isbn);

    // existsByEditora -> SELECT 1 FROM livro WHERE editora_id = ?
    // Usado para impedir deletar uma editora que ainda tem livros vinculados.
    boolean existsByEditora(Editora editora);
}
