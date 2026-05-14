// Pacote dos DTOs de entrada (request) - o que o cliente envia para a API.
package com.biblioteca.api.dto.request;

// Importa as anotações de Bean Validation (@NotBlank, @Size, @Min, etc.).
import jakarta.validation.constraints.*;

// BigDecimal é usado para o preço (valor monetário com precisão).
import java.math.BigDecimal;

/*
 * DTO de entrada para criar/atualizar um Livro.
 * Contém apenas os campos que o cliente precisa enviar no JSON,
 * com as anotações de validação aplicadas em cada um.
 */
public class LivroRequestDTO {

    // @NotBlank: não pode ser nulo nem string vazia/em branco.
    @NotBlank(message = "Título é obrigatório")
    // @Size(max = 200): no máximo 200 caracteres.
    @Size(max = 200)
    private String titulo;

    // ISBN obrigatório.
    @NotBlank(message = "ISBN é obrigatório")
    // @Pattern com regex: exige exatamente 13 dígitos numéricos.
    @Pattern(regexp = "^\\d{13}$", message = "ISBN deve conter exatamente 13 dígitos")
    private String isbn;

    // @NotNull: Integer não aceita @NotBlank (que é só para strings), então usa @NotNull.
    @NotNull(message = "Ano de publicação é obrigatório")
    // @Min: valor mínimo permitido.
    @Min(value = 1500, message = "Ano deve ser maior ou igual a 1500")
    // @Max: valor máximo permitido.
    @Max(value = 2026, message = "Ano deve ser menor ou igual a 2026")
    private Integer anoPublicacao;

    @NotNull(message = "Número de páginas é obrigatório")
    // @Positive: precisa ser maior que zero.
    @Positive(message = "Número de páginas deve ser positivo")
    private Integer numeroPaginas;

    @NotNull(message = "Preço é obrigatório")
    // @DecimalMin: valor decimal mínimo. Aqui exige preço > 0,01.
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal preco;

    // O cliente envia apenas o id da editora; o service busca a entidade pelo id.
    @NotNull(message = "Editora é obrigatória")
    private Long editoraId;

    // Construtor vazio (necessário para o Spring/Jackson desserializar o JSON).
    public LivroRequestDTO() {}

    // Getters e setters: usados pelo Spring/Jackson para preencher os campos a partir do JSON.
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
}
