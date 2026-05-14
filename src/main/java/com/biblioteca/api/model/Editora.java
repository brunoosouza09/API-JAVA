package com.biblioteca.api.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade JPA que representa uma editora de livros no sistema da biblioteca.
 *
 * <p>Mapeada para a tabela {@code editora}. Possui o lado inverso (mappedBy)
 * do relacionamento 1:N com {@link Livro}: uma editora pode publicar vários
 * livros, mas cada livro pertence a uma única editora.
 *
 * <p>O campo {@code nome} é único — não é permitido cadastrar duas editoras
 * com o mesmo nome (regra validada no service e reforçada pela coluna).
 */
@Entity
@Table(name = "editora")
public class Editora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nome;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 80)
    private String pais;

    @OneToMany(mappedBy = "editora")
    private List<Livro> livros = new ArrayList<>();

    public Editora() {}

    /**
     * Construtor de conveniência usado pelos DTOs de request ao converter
     * para entidade.
     *
     * @param nome nome único da editora
     * @param cidade cidade-sede
     * @param pais país-sede
     */
    public Editora(String nome, String cidade, String pais) {
        this.nome = nome;
        this.cidade = cidade;
        this.pais = pais;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public List<Livro> getLivros() { return livros; }
    public void setLivros(List<Livro> livros) { this.livros = livros; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Editora)) return false;
        Editora editora = (Editora) o;
        return Objects.equals(id, editora.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
