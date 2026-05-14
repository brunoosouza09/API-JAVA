package com.biblioteca.api.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entidade JPA que representa um autor de livros.
 *
 * <p>Mapeada para a tabela {@code autor}. Mantém o lado inverso do
 * relacionamento N:N com {@link Livro} via {@code mappedBy = "autores"};
 * a tabela de junção {@code livro_autor} é definida em {@link Livro}.
 *
 * <p>O campo {@code dataNascimento} é opcional, mas, quando informado,
 * deve estar no passado (validação feita no DTO de request).
 */
@Entity
@Table(name = "autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 80)
    private String nacionalidade;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @ManyToMany(mappedBy = "autores")
    private Set<Livro> livros = new HashSet<>();

    public Autor() {}

    /**
     * Construtor de conveniência usado pelos DTOs de request.
     *
     * @param nome nome do autor
     * @param nacionalidade nacionalidade (ex.: "Brasileira")
     * @param dataNascimento data de nascimento (opcional)
     */
    public Autor(String nome, String nacionalidade, LocalDate dataNascimento) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.dataNascimento = dataNascimento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public Set<Livro> getLivros() { return livros; }
    public void setLivros(Set<Livro> livros) { this.livros = livros; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Autor)) return false;
        Autor autor = (Autor) o;
        return Objects.equals(id, autor.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
