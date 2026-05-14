package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Categoria;

import java.util.List;

/**
 * DTO de saída completo de uma categoria.
 *
 * <p>Inclui id, nome, descrição e a lista de livros vinculados em formato
 * resumido ({@link LivroResumoDTO}).
 */
public class CategoriaResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private List<LivroResumoDTO> livros;

    public CategoriaResponseDTO() {}

    /**
     * Converte a entidade {@link Categoria} no DTO de resposta completo,
     * já mapeando os livros para o formato resumido.
     *
     * @param c categoria de origem
     * @return DTO pronto para serialização
     */
    public static CategoriaResponseDTO fromEntity(Categoria c) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.id = c.getId();
        dto.nome = c.getNome();
        dto.descricao = c.getDescricao();
        dto.livros = c.getLivros().stream().map(LivroResumoDTO::fromEntity).toList();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<LivroResumoDTO> getLivros() { return livros; }
    public void setLivros(List<LivroResumoDTO> livros) { this.livros = livros; }
}
