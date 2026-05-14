package com.biblioteca.api.dto.request;

import com.biblioteca.api.model.Editora;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EditoraRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100)
    private String cidade;

    @NotBlank(message = "País é obrigatório")
    @Size(max = 80)
    private String pais;

    public EditoraRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public Editora toEntity() {
        return new Editora(nome, cidade, pais);
    }

    public void applyTo(Editora editora) {
        editora.setNome(nome);
        editora.setCidade(cidade);
        editora.setPais(pais);
    }
}
