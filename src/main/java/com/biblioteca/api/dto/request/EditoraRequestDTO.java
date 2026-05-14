package com.biblioteca.api.dto.request;

import com.biblioteca.api.model.Editora;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada usado para criar ou atualizar uma {@link Editora}.
 *
 * <p>Existe separado da entidade para:
 * <ul>
 *   <li>aplicar validações de Bean Validation no payload (NotBlank, Size);</li>
 *   <li>evitar expor diretamente a entidade JPA na API;</li>
 *   <li>desacoplar o contrato do cliente do mapeamento do banco.</li>
 * </ul>
 */
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

    /**
     * Converte este DTO em uma nova instância de {@link Editora}
     * (usado no fluxo de criação).
     *
     * @return entidade preenchida com os dados do DTO
     */
    public Editora toEntity() {
        return new Editora(nome, cidade, pais);
    }

    /**
     * Copia os campos deste DTO para uma editora existente
     * (usado no fluxo de atualização).
     *
     * @param editora entidade que será atualizada
     */
    public void applyTo(Editora editora) {
        editora.setNome(nome);
        editora.setCidade(cidade);
        editora.setPais(pais);
    }
}
