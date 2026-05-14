// Pacote dos DTOs de saída.
package com.biblioteca.api.dto.response;

// Importa a entidade Editora para conseguir converter entidade -> DTO.
import com.biblioteca.api.model.Editora;

/*
 * DTO de saída de uma Editora.
 * Contém apenas os campos cadastrais (id, nome, cidade, país),
 * sem expor diretamente a entidade JPA.
 */
public class EditoraResponseDTO {

    // Identificador da editora.
    private Long id;
    // Nome da editora.
    private String nome;
    // Cidade-sede.
    private String cidade;
    // País-sede.
    private String pais;

    // Construtor vazio.
    public EditoraResponseDTO() {}

    // Converte a entidade Editora em um DTO de resposta.
    public static EditoraResponseDTO fromEntity(Editora e) {
        // Cria um DTO vazio para preencher.
        EditoraResponseDTO dto = new EditoraResponseDTO();
        // Copia cada campo da entidade para o DTO.
        dto.id = e.getId();
        dto.nome = e.getNome();
        dto.cidade = e.getCidade();
        dto.pais = e.getPais();
        // Retorna o DTO pronto para virar JSON.
        return dto;
    }

    // Getters e setters padrão.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
}
