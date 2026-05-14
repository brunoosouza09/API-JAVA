package com.biblioteca.api.dto.request;

import com.biblioteca.api.model.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 80)
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    public CategoriaRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Categoria toEntity() {
        return new Categoria(nome, descricao);
    }

    public void applyTo(Categoria categoria) {
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
    }
}
