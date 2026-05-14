package com.biblioteca.api.dto.request;

import com.biblioteca.api.model.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para criação/atualização de uma {@link Categoria}.
 *
 * <p>Carrega o nome (obrigatório) e a descrição (opcional) recebidos do
 * cliente, aplicando validações de Bean Validation antes de chegar ao
 * service.
 */
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

    /**
     * Converte este DTO em uma nova {@link Categoria} (criação).
     *
     * @return entidade preenchida com os dados do DTO
     */
    public Categoria toEntity() {
        return new Categoria(nome, descricao);
    }

    /**
     * Aplica os valores deste DTO em uma categoria existente (atualização).
     *
     * @param categoria entidade que será atualizada
     */
    public void applyTo(Categoria categoria) {
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
    }
}
