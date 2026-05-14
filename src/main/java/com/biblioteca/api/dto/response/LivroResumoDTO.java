package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Livro;

public class LivroResumoDTO {

    private Long id;
    private String titulo;
    private String isbn;

    public LivroResumoDTO() {}

    public LivroResumoDTO(Long id, String titulo, String isbn) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
    }

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
