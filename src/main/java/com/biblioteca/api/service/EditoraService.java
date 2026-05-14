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

@Service
public class EditoraService {

    private final EditoraRepository editoraRepository;
    private final LivroRepository livroRepository;

    public EditoraService(EditoraRepository editoraRepository, LivroRepository livroRepository) {
        this.editoraRepository = editoraRepository;
        this.livroRepository = livroRepository;
    }

    @Transactional(readOnly = true)
    public List<EditoraResponseDTO> listar() {
        return editoraRepository.findAll().stream()
                .map(EditoraResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public EditoraResponseDTO buscarPorId(Long id) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
        return EditoraResponseDTO.fromEntity(editora);
    }

    @Transactional
    public EditoraResponseDTO criar(EditoraRequestDTO dto) {
        if (editoraRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe uma editora com o nome '" + dto.getNome() + "'");
        }
        Editora salva = editoraRepository.save(dto.toEntity());
        return EditoraResponseDTO.fromEntity(salva);
    }

    @Transactional
    public EditoraResponseDTO atualizar(Long id, EditoraRequestDTO dto) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
        if (!editora.getNome().equalsIgnoreCase(dto.getNome())
                && editoraRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe uma editora com o nome '" + dto.getNome() + "'");
        }
        dto.applyTo(editora);
        return EditoraResponseDTO.fromEntity(editora);
    }

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
