// Declara o pacote onde esta classe está localizada (model = entidades JPA).
package com.biblioteca.api.model;

// Importa todas as anotações do JPA (@Entity, @Table, @Id, @Column, etc.).
import jakarta.persistence.*;

// BigDecimal é usado para representar valores monetários (preço) com precisão.
import java.math.BigDecimal;
// Objects.equals e Objects.hash facilitam implementar equals/hashCode.
import java.util.Objects;

// @Entity marca esta classe como uma entidade JPA, ou seja, ela é mapeada para uma tabela do banco.
@Entity
// @Table define o nome da tabela no banco (caso queiramos diferente do nome da classe).
@Table(name = "livro")
public class Livro {

    // @Id indica que este campo é a chave primária da tabela.
    @Id
    // @GeneratedValue faz o banco gerar o id automaticamente (auto-incremento).
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column configura a coluna: não pode ser nula e tem no máximo 200 caracteres.
    @Column(nullable = false, length = 200)
    private String titulo;

    // ISBN é único: o banco não permite cadastrar dois livros com o mesmo ISBN.
    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    // name = "ano_publicacao" personaliza o nome da coluna no banco (snake_case).
    @Column(name = "ano_publicacao", nullable = false)
    private Integer anoPublicacao;

    // Mesma ideia: nome da coluna no banco é "numero_paginas".
    @Column(name = "numero_paginas", nullable = false)
    private Integer numeroPaginas;

    // precision = total de dígitos; scale = quantos depois da vírgula. Ex: 99999999.99
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    // @ManyToOne: muitos livros podem pertencer a uma mesma editora (relação N:1).
    // optional = false: a editora é obrigatória.
    // O fetch fica EAGER (padrão do @ManyToOne): a editora é carregada junto
    // com o livro — necessário porque os DTOs leem editora.getNome() fora do service.
    @ManyToOne(optional = false)
    // @JoinColumn cria a coluna "editora_id" no banco, que é a chave estrangeira.
    @JoinColumn(name = "editora_id", nullable = false)
    private Editora editora;

    // Construtor vazio exigido pelo JPA para conseguir instanciar a entidade.
    public Livro() {}

    // Daqui em diante: getters e setters padrão (Spring/JPA usam para ler/escrever os campos).
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public Integer getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(Integer numeroPaginas) { this.numeroPaginas = numeroPaginas; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Editora getEditora() { return editora; }
    public void setEditora(Editora editora) { this.editora = editora; }

    // equals/hashCode baseados no id: duas entidades são consideradas iguais se têm o mesmo id.
    @Override
    public boolean equals(Object o) {
        // Se for o mesmo objeto na memória, já é igual.
        if (this == o) return true;
        // Se o outro não for um Livro, não pode ser igual.
        if (!(o instanceof Livro)) return false;
        // Faz o cast e compara apenas o id.
        Livro livro = (Livro) o;
        return Objects.equals(id, livro.id);
    }

    // hashCode também só usa o id, mantendo a coerência com equals.
    @Override
    public int hashCode() { return Objects.hash(id); }
}
