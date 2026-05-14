package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.AutorRequestDTO;
import com.biblioteca.api.dto.response.AutorResponseDTO;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.model.Autor;
import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.AutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço para o recurso Autor.
 *
 * <p>Não há restrição de unicidade por nome (dois autores podem ter o
 * mesmo nome em homenagens/coincidências), então as operações de criação
 * e atualização não fazem checagem de duplicidade. A exclusão remove
 * antes o vínculo N:N nos livros que referenciam o autor.
 */
@Service
public class AutorService {

    private final AutorRepository autorRepository;

    /**
     * Construtor com injeção do repositório de autores.
     */
    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    /**
     * Lista todos os autores.
     *
     * @return lista (possivelmente vazia) com todos os autores
     */
    @Transactional(readOnly = true)
    public List<AutorResponseDTO> listar() {
        return autorRepository.findAll().stream()
                .map(AutorResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Busca um autor pelo id.
     *
     * @param id identificador do autor
     * @return DTO do autor encontrado
     * @throws ResourceNotFoundException se não houver autor com esse id
     */
    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(Long id) {
        Autor a = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));
        return AutorResponseDTO.fromEntity(a);
    }

    /**
     * Cria um novo autor a partir do DTO de entrada.
     *
     * @param dto dados validados
     * @return DTO do autor recém-criado
     */
    @Transactional
    public AutorResponseDTO criar(AutorRequestDTO dto) {
        Autor salvo = autorRepository.save(dto.toEntity());
        return AutorResponseDTO.fromEntity(salvo);
    }

    /**
     * Atualiza um autor existente.
     *
     * @param id identificador do autor
     * @param dto novos valores
     * @return DTO do autor atualizado
     * @throws ResourceNotFoundException se o autor não existir
     */
    @Transactional
    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        Autor a = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));
        dto.applyTo(a);
        return AutorResponseDTO.fromEntity(a);
    }

    /**
     * Remove um autor, primeiro desfazendo o vínculo N:N nos livros que
     * o referenciam (evita violar integridade da tabela de junção).
     *
     * @param id identificador do autor
     * @throws ResourceNotFoundException se o autor não existir
     */
    @Transactional
    public void deletar(Long id) {
        Autor a = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));
        // desfaz o vínculo nos livros antes de remover
        for (Livro livro : a.getLivros()) {
            livro.getAutores().remove(a);
        }
        autorRepository.delete(a);
    }
}
