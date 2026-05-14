package com.biblioteca.api.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public class LivroRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200)
    private String titulo;

    @NotBlank(message = "ISBN é obrigatório")
    @Pattern(regexp = "^\\d{13}$", message = "ISBN deve conter exatamente 13 dígitos")
    private String isbn;

    @NotNull(message = "Ano de publicação é obrigatório")
    @Min(value = 1500, message = "Ano deve ser maior ou igual a 1500")
    @Max(value = 2026, message = "Ano deve ser menor ou igual a 2026")
    private Integer anoPublicacao;

    @NotNull(message = "Número de páginas é obrigatório")
    @Positive(message = "Número de páginas deve ser positivo")
    private Integer numeroPaginas;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "Editora é obrigatória")
    private Long editoraId;

    @NotEmpty(message = "Informe ao menos um autor")
    private Set<Long> autoresIds;

    @NotEmpty(message = "Informe ao menos uma categoria")
    private Set<Long> categoriasIds;

    public LivroRequestDTO() {}

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

    public Long getEditoraId() { return editoraId; }
    public void setEditoraId(Long editoraId) { this.editoraId = editoraId; }

    public Set<Long> getAutoresIds() { return autoresIds; }
    public void setAutoresIds(Set<Long> autoresIds) { this.autoresIds = autoresIds; }

    public Set<Long> getCategoriasIds() { return categoriasIds; }
    public void setCategoriasIds(Set<Long> categoriasIds) { this.categoriasIds = categoriasIds; }
}
