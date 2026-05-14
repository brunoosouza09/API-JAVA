package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Livro;

/**
 * DTO "resumido" de um livro, usado dentro de outras respostas
 * (ex.: lista de livros de uma editora, autor ou categoria) para evitar
 * payloads aninhados gigantes e ciclos no JSON.
 *
 * <p>Contém apenas id, título e ISBN — o suficiente para identificar
 * o livro sem expandir todos os seus relacionamentos.
 */
public class LivroResumoDTO {

    private Long id;
    private String titulo;
    private String isbn;

    public LivroResumoDTO() {}

    /**
     * Construtor completo do resumo.
     *
     * @param id id do livro
     * @param titulo título do livro
     * @param isbn ISBN do livro
     */
    public LivroResumoDTO(Long id, String titulo, String isbn) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
    }

    /**
     * Cria um resumo a partir da entidade {@link Livro}.
     *
     * @param l entidade de origem
     * @return DTO resumido equivalente
     */
    public static LivroResumoDTO fromEntity(Livro l) {
        return new LivroResumoDTO(l.getId(), l.getTitulo(), l.getIsbn());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
}
