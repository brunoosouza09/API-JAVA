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

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Transactional(readOnly = true)
    public List<AutorResponseDTO> listar() {
        return autorRepository.findAll().stream()
                .map(AutorResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(Long id) {
        Autor a = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));
        return AutorResponseDTO.fromEntity(a);
    }

    @Transactional
    public AutorResponseDTO criar(AutorRequestDTO dto) {
        Autor salvo = autorRepository.save(dto.toEntity());
        return AutorResponseDTO.fromEntity(salvo);
    }

    @Transactional
    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        Autor a = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));
        dto.applyTo(a);
        return AutorResponseDTO.fromEntity(a);
    }

    @Transactional
    public void deletar(Long id) {
        Autor a = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));
        for (Livro livro : a.getLivros()) {
            livro.getAutores().remove(a);
        }
        autorRepository.delete(a);
    }
}
