package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Autor;

public class AutorResumoDTO {

    private Long id;
    private String nome;

    public AutorResumoDTO() {}

    public AutorResumoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static AutorResumoDTO fromEntity(Autor a) {
        return new AutorResumoDTO(a.getId(), a.getNome());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
