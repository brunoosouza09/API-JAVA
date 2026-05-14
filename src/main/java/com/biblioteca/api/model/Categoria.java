package com.biblioteca.api.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entidade JPA que representa uma categoria/gênero de livro
 * (ex.: "Ficção", "Tecnologia").
 *
 * <p>Mapeada para a tabela {@code categoria}. Mantém o lado inverso do
 * relacionamento N:N com {@link Livro} via {@code mappedBy = "categorias"};
 * a tabela de junção {@code livro_categoria} é definida em {@link Livro}.
 *
 * <p>O campo {@code nome} é único (não pode haver categorias duplicadas).
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @ManyToMany(mappedBy = "categorias")
    private Set<Livro> livros = new HashSet<>();

    public Categoria() {}

    /**
     * Construtor de conveniência usado pelos DTOs de request.
     *
     * @param nome nome único da categoria
     * @param descricao descrição livre (pode ser nula)
     */
    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Set<Livro> getLivros() { return livros; }
    public void setLivros(Set<Livro> livros) { this.livros = livros; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria that = (Categoria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
