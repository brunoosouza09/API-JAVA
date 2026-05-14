package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Editora;

import java.util.List;

public class EditoraResponseDTO {

    private Long id;
    private String nome;
    private String cidade;
    private String pais;
    private List<LivroResumoDTO> livros;

    public EditoraResponseDTO() {}

    public static EditoraResponseDTO fromEntity(Editora e) {
        EditoraResponseDTO dto = new EditoraResponseDTO();
        dto.id = e.getId();
        dto.nome = e.getNome();
        dto.cidade = e.getCidade();
        dto.pais = e.getPais();
        dto.livros = e.getLivros().stream().map(LivroResumoDTO::fromEntity).toList();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public List<LivroResumoDTO> getLivros() { return livros; }
    public void setLivros(List<LivroResumoDTO> livros) { this.livros = livros; }
}
