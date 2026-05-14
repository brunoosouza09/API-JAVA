// Pacote onde a entidade Editora está organizada.
package com.biblioteca.api.model;

// Importa as anotações JPA (@Entity, @Table, @Id, @Column, etc.).
import jakarta.persistence.*;

// Objects.equals e Objects.hash ajudam a implementar equals/hashCode.
import java.util.Objects;

// @Entity = classe mapeada para uma tabela do banco.
@Entity
// @Table define o nome da tabela como "editora".
@Table(name = "editora")
public class Editora {

    // Chave primária da tabela.
    @Id
    // Geração automática (auto-incremento do banco).
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Coluna obrigatória, valor único (não pode repetir nome), máximo 150 caracteres.
    @Column(nullable = false, unique = true, length = 150)
    private String nome;

    // Cidade-sede da editora. Obrigatória, máximo 100 caracteres.
    @Column(nullable = false, length = 100)
    private String cidade;

    // País-sede da editora. Obrigatório, máximo 80 caracteres.
    @Column(nullable = false, length = 80)
    private String pais;

    // Construtor vazio exigido pelo JPA.
    public Editora() {}

    // Construtor de conveniência: facilita criar uma Editora a partir do DTO de entrada.
    public Editora(String nome, String cidade, String pais) {
        // Atribui o nome recebido ao atributo da instância.
        this.nome = nome;
        // Atribui a cidade.
        this.cidade = cidade;
        // Atribui o país.
        this.pais = pais;
    }

    // Getters e setters padrão.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    // Comparação por id (padrão JPA): duas entidades são iguais se tiverem o mesmo id.
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
