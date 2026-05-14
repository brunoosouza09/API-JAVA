package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Autor;

import java.time.LocalDate;
import java.util.List;

public class AutorResponseDTO {

    private Long id;
    private String nome;
    private String nacionalidade;
    private LocalDate dataNascimento;
    private List<LivroResumoDTO> livros;

    public AutorResponseDTO() {}

    public static AutorResponseDTO fromEntity(Autor a) {
        AutorResponseDTO dto = new AutorResponseDTO();
        dto.id = a.getId();
        dto.nome = a.getNome();
        dto.nacionalidade = a.getNacionalidade();
        dto.dataNascimento = a.getDataNascimento();
        dto.livros = a.getLivros().stream().map(LivroResumoDTO::fromEntity).toList();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public List<LivroResumoDTO> getLivros() { return livros; }
    public void setLivros(List<LivroResumoDTO> livros) { this.livros = livros; }
}
