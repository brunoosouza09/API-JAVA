package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Categoria;

public class CategoriaResumoDTO {

    private Long id;
    private String nome;

    public CategoriaResumoDTO() {}

    public CategoriaResumoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static CategoriaResumoDTO fromEntity(Categoria c) {
        return new CategoriaResumoDTO(c.getId(), c.getNome());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
