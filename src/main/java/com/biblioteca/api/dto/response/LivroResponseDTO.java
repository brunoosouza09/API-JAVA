// Pacote dos DTOs de saída (response) - o que a API devolve para o cliente.
package com.biblioteca.api.dto.response;

// Importa a entidade Livro para conseguir converter entidade -> DTO.
import com.biblioteca.api.model.Livro;

// BigDecimal para representar o preço.
import java.math.BigDecimal;

/*
 * DTO de saída de um Livro.
 * Carrega os dados do próprio livro + os dados básicos da editora
 * (id e nome), sem expor diretamente a entidade JPA.
 */
public class LivroResponseDTO {

    // Identificador do livro.
    private Long id;
    // Título do livro.
    private String titulo;
    // ISBN (13 dígitos).
    private String isbn;
    // Ano de publicação.
    private Integer anoPublicacao;
    // Quantidade de páginas.
    private Integer numeroPaginas;
    // Preço de capa.
    private BigDecimal preco;
    // Id da editora vinculada (relação N:1).
    private Long editoraId;
    // Nome da editora vinculada (carregado para ser exibido junto com o livro).
    private String editoraNome;

    // Construtor vazio.
    public LivroResponseDTO() {}

    // Método estático de conversão: recebe a entidade e devolve o DTO equivalente.
    public static LivroResponseDTO fromEntity(Livro l) {
        // Cria um DTO vazio que será preenchido com os dados da entidade.
        LivroResponseDTO dto = new LivroResponseDTO();
        // Copia o id do livro.
        dto.id = l.getId();
        // Copia o título.
        dto.titulo = l.getTitulo();
        // Copia o ISBN.
        dto.isbn = l.getIsbn();
        // Copia o ano de publicação.
        dto.anoPublicacao = l.getAnoPublicacao();
        // Copia o número de páginas.
        dto.numeroPaginas = l.getNumeroPaginas();
        // Copia o preço.
        dto.preco = l.getPreco();
        // Copia o id da editora (a entidade Editora vem associada via @ManyToOne).
        dto.editoraId = l.getEditora().getId();
        // Copia o nome da editora para exibir junto ao livro.
        dto.editoraNome = l.getEditora().getNome();
        // Retorna o DTO pronto para ser serializado em JSON.
        return dto;
    }

    // Getters e setters (Spring/Jackson usam para gerar o JSON).
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

    public Long getEditoraId() { return editoraId; }
    public void setEditoraId(Long editoraId) { this.editoraId = editoraId; }

    public String getEditoraNome() { return editoraNome; }
    public void setEditoraNome(String editoraNome) { this.editoraNome = editoraNome; }
}
