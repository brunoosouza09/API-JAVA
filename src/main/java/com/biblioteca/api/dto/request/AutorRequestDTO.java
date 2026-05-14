package com.biblioteca.api.dto.request;

import com.biblioteca.api.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO de entrada para criação/atualização de um {@link Autor}.
 *
 * <p>Encapsula nome, nacionalidade e data de nascimento (opcional, mas
 * obrigatoriamente no passado quando informada). Mantido separado da
 * entidade JPA para que o contrato da API não dependa do mapeamento ORM.
 */
public class AutorRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150)
    private String nome;

    @NotBlank(message = "Nacionalidade é obrigatória")
    @Size(max = 80)
    private String nacionalidade;

    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    public AutorRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    /**
     * Converte este DTO em uma nova entidade {@link Autor} (criação).
     *
     * @return entidade preenchida com os dados do DTO
     */
    public Autor toEntity() {
        return new Autor(nome, nacionalidade, dataNascimento);
    }

    /**
     * Aplica os valores deste DTO em um autor existente (atualização).
     *
     * @param autor entidade que será atualizada
     */
    public void applyTo(Autor autor) {
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);
        autor.setDataNascimento(dataNascimento);
    }
}
