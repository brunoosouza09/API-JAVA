// Pacote dos DTOs de entrada da API.
package com.biblioteca.api.dto.request;

// Importa a entidade Editora para conseguir converter DTO -> entidade.
import com.biblioteca.api.model.Editora;
// Anotações de validação que serão aplicadas nos campos.
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * DTO de entrada para criar/atualizar uma Editora.
 * É uma classe separada da entidade JPA para:
 *  - aplicar validações de Bean Validation no payload;
 *  - não expor a entidade JPA diretamente no contrato da API.
 */
public class EditoraRequestDTO {

    // Nome obrigatório, no máximo 150 caracteres.
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String nome;

    // Cidade obrigatória, no máximo 100 caracteres.
    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100)
    private String cidade;

    // País obrigatório, no máximo 80 caracteres.
    @NotBlank(message = "País é obrigatório")
    @Size(max = 80)
    private String pais;

    // Construtor vazio (necessário para o Spring/Jackson preencher os campos via JSON).
    public EditoraRequestDTO() {}

    // Getters e setters padrão.
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    // Converte este DTO em uma nova Editora (usado na criação).
    public Editora toEntity() {
        // Cria uma nova entidade já preenchida com os dados do DTO.
        return new Editora(nome, cidade, pais);
    }

    // Copia os campos do DTO para uma Editora existente (usado na atualização).
    public void applyTo(Editora editora) {
        editora.setNome(nome);
        editora.setCidade(cidade);
        editora.setPais(pais);
    }
}
