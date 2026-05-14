package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Livro;

import java.math.BigDecimal;
import java.util.List;

public class LivroResponseDTO {

    private Long id;
    private String titulo;
    private String isbn;
    private Integer anoPublicacao;
    private Integer numeroPaginas;
    private BigDecimal preco;
    private EditoraResumoDTO editora;
    private List<AutorResumoDTO> autores;
    private List<CategoriaResumoDTO> categorias;

    public LivroResponseDTO() {}

    public static LivroResponseDTO fromEntity(Livro l) {
        LivroResponseDTO dto = new LivroResponseDTO();
        dto.id = l.getId();
        dto.titulo = l.getTitulo();
        dto.isbn = l.getIsbn();
        dto.anoPublicacao = l.getAnoPublicacao();
        dto.numeroPaginas = l.getNumeroPaginas();
        dto.preco = l.getPreco();
        dto.editora = EditoraResumoDTO.fromEntity(l.getEditora());
        dto.autores = l.getAutores().stream().map(AutorResumoDTO::fromEntity).toList();
        dto.categorias = l.getCategorias().stream().map(CategoriaResumoDTO::fromEntity).toList();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public Integer getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(Integer numeroPaginas) { this.numeroPaginas = numeroPaginas; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public EditoraResumoDTO getEditora() { return editora; }
    public void setEditora(EditoraResumoDTO editora) { this.editora = editora; }

    public List<AutorResumoDTO> getAutores() { return autores; }
    public void setAutores(List<AutorResumoDTO> autores) { this.autores = autores; }

    public List<CategoriaResumoDTO> getCategorias() { return categorias; }
    public void setCategorias(List<CategoriaResumoDTO> categorias) { this.categorias = categorias; }
}
