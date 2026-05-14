package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.EditoraRequestDTO;
import com.biblioteca.api.dto.response.EditoraResponseDTO;
import com.biblioteca.api.exception.BusinessException;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.model.Editora;
import com.biblioteca.api.repository.EditoraRepository;
import com.biblioteca.api.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço para o recurso Editora.
 *
 * <p>Concentra as regras de negócio: garante nome único (na criação e na
 * atualização) e impede a exclusão de editoras que ainda possuam livros
 * vinculados. Também converte entre entidade e DTOs.
 */
@Service
public class EditoraService {

    private final EditoraRepository editoraRepository;
    private final LivroRepository livroRepository;

    /**
     * Construtor com injeção dos repositórios usados pelas regras
     * (editora para CRUD e livro para a verificação de vínculo).
     */
    public EditoraService(EditoraRepository editoraRepository, LivroRepository livroRepository) {
        this.editoraRepository = editoraRepository;
        this.livroRepository = livroRepository;
    }

    /**
     * Lista todas as editoras como DTOs de resposta.
     *
     * @return lista (possivelmente vazia) com todas as editoras
     */
    @Transactional(readOnly = true)
    public List<EditoraResponseDTO> listar() {
        return editoraRepository.findAll().stream()
                .map(EditoraResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Busca uma editora pelo id.
     *
     * @param id identificador da editora
     * @return DTO de resposta da editora encontrada
     * @throws ResourceNotFoundException se não houver editora com esse id
     */
    @Transactional(readOnly = true)
    public EditoraResponseDTO buscarPorId(Long id) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
        return EditoraResponseDTO.fromEntity(editora);
    }

    /**
     * Cria uma nova editora, rejeitando nomes já cadastrados.
     *
     * @param dto dados de entrada validados
     * @return DTO da editora recém-criada
     * @throws BusinessException se já existir editora com o mesmo nome
     */
    @Transactional
    public EditoraResponseDTO criar(EditoraRequestDTO dto) {
        if (editoraRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe uma editora com o nome '" + dto.getNome() + "'");
        }
        Editora salva = editoraRepository.save(dto.toEntity());
        return EditoraResponseDTO.fromEntity(salva);
    }

    /**
     * Atualiza uma editora existente.
     *
     * @param id identificador da editora
     * @param dto novos valores
     * @return DTO da editora atualizada
     * @throws ResourceNotFoundException se a editora não existir
     * @throws BusinessException se o novo nome colidir com outra editora
     */
    @Transactional
    public EditoraResponseDTO atualizar(Long id, EditoraRequestDTO dto) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
        // só verifica duplicidade se o nome realmente mudou
        if (!editora.getNome().equalsIgnoreCase(dto.getNome())
                && editoraRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe uma editora com o nome '" + dto.getNome() + "'");
        }
        dto.applyTo(editora);
        return EditoraResponseDTO.fromEntity(editora);
    }

    /**
     * Remove uma editora pelo id.
     *
     * @param id identificador da editora
     * @throws ResourceNotFoundException se não houver editora com esse id
     * @throws BusinessException se houver livros vinculados a essa editora
     */
    @Transactional
    public void deletar(Long id) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
        if (livroRepository.existsByEditora(editora)) {
            throw new BusinessException("Não é possível remover editora com livros vinculados");
        }
        editoraRepository.delete(editora);
    }
}
