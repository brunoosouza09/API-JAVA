package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Editora;

/**
 * DTO resumido de editora, usado dentro do {@link LivroResponseDTO}
 * para representar a editora sem expandir a lista de livros dela
 * (evita ciclos e payloads excessivos).
 */
public class EditoraResumoDTO {

    private Long id;
    private String nome;

    public EditoraResumoDTO() {}

    /**
     * Construtor completo do resumo de editora.
     *
     * @param id id da editora
     * @param nome nome da editora
     */
    public EditoraResumoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    /**
     * Cria um resumo a partir da entidade {@link Editora}.
     *
     * @param e entidade de origem
     * @return DTO resumido equivalente
     */
    public static EditoraResumoDTO fromEntity(Editora e) {
        return new EditoraResumoDTO(e.getId(), e.getNome());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
