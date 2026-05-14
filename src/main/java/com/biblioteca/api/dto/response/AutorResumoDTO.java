package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Autor;

/**
 * DTO resumido de autor, usado dentro do {@link LivroResponseDTO}
 * para listar os autores do livro sem expandir os livros de cada autor
 * (evita ciclos e payloads excessivos).
 */
public class AutorResumoDTO {

    private Long id;
    private String nome;

    public AutorResumoDTO() {}

    /**
     * Construtor completo do resumo de autor.
     *
     * @param id id do autor
     * @param nome nome do autor
     */
    public AutorResumoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    /**
     * Cria um resumo a partir da entidade {@link Autor}.
     *
     * @param a entidade de origem
     * @return DTO resumido equivalente
     */
    public static AutorResumoDTO fromEntity(Autor a) {
        return new AutorResumoDTO(a.getId(), a.getNome());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
