package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Editora;

public class EditoraResumoDTO {

    private Long id;
    private String nome;

    public EditoraResumoDTO() {}

    public EditoraResumoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static EditoraResumoDTO fromEntity(Editora e) {
        return new EditoraResumoDTO(e.getId(), e.getNome());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
