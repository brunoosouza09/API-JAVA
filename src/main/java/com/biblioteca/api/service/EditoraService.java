// Pacote da camada de serviço.
package com.biblioteca.api.service;

// DTO de entrada (request) da Editora.
import com.biblioteca.api.dto.request.EditoraRequestDTO;
// DTO de saída (response) da Editora.
import com.biblioteca.api.dto.response.EditoraResponseDTO;
// Exceção para regras de negócio violadas.
import com.biblioteca.api.exception.BusinessException;
// Exceção para recurso inexistente (404).
import com.biblioteca.api.exception.ResourceNotFoundException;
// Entidade Editora.
import com.biblioteca.api.model.Editora;
// Repositório da Editora.
import com.biblioteca.api.repository.EditoraRepository;
// Repositório de Livro (usado para impedir deletar editora com livros vinculados).
import com.biblioteca.api.repository.LivroRepository;
// @Service registra a classe como componente de serviço no Spring.
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Camada de serviço de Editora.
 * Contém as regras: nome único e impedimento de exclusão quando há
 * livros vinculados.
 */
@Service
public class EditoraService {

    // Repositório usado para acessar a tabela de editoras.
    private final EditoraRepository editoraRepository;
    // Repositório de livro para checar se a editora tem livros (impede deleção).
    private final LivroRepository livroRepository;

    // Construtor com injeção de dependências (Spring preenche automaticamente).
    public EditoraService(EditoraRepository editoraRepository, LivroRepository livroRepository) {
        this.editoraRepository = editoraRepository;
        this.livroRepository = livroRepository;
    }

    // Lista todas as editoras como DTOs.
    public List<EditoraResponseDTO> listar() {
        // findAll() devolve todas as editoras; cada uma é convertida em DTO.
        return editoraRepository.findAll().stream()
                .map(EditoraResponseDTO::fromEntity)
                .toList();
    }

    // Busca uma editora pelo id (lança 404 se não existir).
    public EditoraResponseDTO buscarPorId(Long id) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
        return EditoraResponseDTO.fromEntity(editora);
    }

    // Cria uma nova editora.
    public EditoraResponseDTO criar(EditoraRequestDTO dto) {
        // Regra: não pode haver duas editoras com o mesmo nome (case-insensitive).
        if (editoraRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe uma editora com o nome '" + dto.getNome() + "'");
        }
        // Converte o DTO em entidade e salva.
        Editora salva = editoraRepository.save(dto.toEntity());
        // Devolve o DTO da editora recém-criada (já com id preenchido).
        return EditoraResponseDTO.fromEntity(salva);
    }

    // Atualiza uma editora existente.
    public EditoraResponseDTO atualizar(Long id, EditoraRequestDTO dto) {
        // Busca a entidade; 404 se não existir.
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
        // Só verifica duplicidade se o nome realmente mudou.
        if (!editora.getNome().equalsIgnoreCase(dto.getNome())
                && editoraRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe uma editora com o nome '" + dto.getNome() + "'");
        }
        // Copia os campos do DTO para a entidade.
        dto.applyTo(editora);
        // Salva e devolve a entidade atualizada.
        Editora atualizada = editoraRepository.save(editora);
        return EditoraResponseDTO.fromEntity(atualizada);
    }

    // Remove uma editora pelo id.
    public void deletar(Long id) {
        // Busca a editora; 404 se não existir.
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
        // Regra: não permite deletar se houver livros vinculados a essa editora.
        if (livroRepository.existsByEditora(editora)) {
            throw new BusinessException("Não é possível remover editora com livros vinculados");
        }
        // Tudo certo: deleta do banco.
        editoraRepository.delete(editora);
    }
}
