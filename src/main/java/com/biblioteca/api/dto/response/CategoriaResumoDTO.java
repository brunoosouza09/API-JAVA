package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Categoria;

/**
 * DTO resumido de categoria, usado dentro do {@link LivroResponseDTO}
 * para listar as categorias do livro sem expandir os livros vinculados
 * a cada categoria.
 */
public class CategoriaResumoDTO {

    private Long id;
    private String nome;

    public CategoriaResumoDTO() {}

    /**
     * Construtor completo do resumo de categoria.
     *
     * @param id id da categoria
     * @param nome nome da categoria
     */
    public CategoriaResumoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    /**
     * Cria um resumo a partir da entidade {@link Categoria}.
     *
     * @param c entidade de origem
     * @return DTO resumido equivalente
     */
    public static CategoriaResumoDTO fromEntity(Categoria c) {
        return new CategoriaResumoDTO(c.getId(), c.getNome());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
