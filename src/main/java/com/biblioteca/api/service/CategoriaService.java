package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.CategoriaRequestDTO;
import com.biblioteca.api.dto.response.CategoriaResponseDTO;
import com.biblioteca.api.exception.BusinessException;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.model.Categoria;
import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço para o recurso Categoria.
 *
 * <p>Implementa as regras: nome único (criação e atualização) e remoção
 * "segura" — antes de excluir, desfaz a associação N:N nos livros que
 * referenciam a categoria, evitando violar a integridade da tabela
 * {@code livro_categoria}.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    /**
     * Construtor com injeção do repositório de categorias.
     */
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Lista todas as categorias.
     *
     * @return lista (possivelmente vazia) com todas as categorias
     */
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Busca uma categoria pelo id.
     *
     * @param id identificador da categoria
     * @return DTO da categoria encontrada
     * @throws ResourceNotFoundException se não houver categoria com esse id
     */
    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria c = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        return CategoriaResponseDTO.fromEntity(c);
    }

    /**
     * Cria uma nova categoria, rejeitando nomes duplicados.
     *
     * @param dto dados validados de entrada
     * @return DTO da categoria recém-criada
     * @throws BusinessException se já existir categoria com o mesmo nome
     */
    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe categoria com o nome '" + dto.getNome() + "'");
        }
        Categoria salva = categoriaRepository.save(dto.toEntity());
        return CategoriaResponseDTO.fromEntity(salva);
    }

    /**
     * Atualiza uma categoria existente.
     *
     * @param id identificador da categoria
     * @param dto novos valores
     * @return DTO da categoria atualizada
     * @throws ResourceNotFoundException se a categoria não existir
     * @throws BusinessException se o novo nome colidir com outra categoria
     */
    @Transactional
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria c = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        // só verifica duplicidade se o nome realmente mudou
        if (!c.getNome().equalsIgnoreCase(dto.getNome())
                && categoriaRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe categoria com o nome '" + dto.getNome() + "'");
        }
        dto.applyTo(c);
        return CategoriaResponseDTO.fromEntity(c);
    }

    /**
     * Remove uma categoria, primeiro desfazendo o vínculo N:N nos livros
     * que a referenciam (para não violar integridade da tabela de junção).
     *
     * @param id identificador da categoria
     * @throws ResourceNotFoundException se a categoria não existir
     */
    @Transactional
    public void deletar(Long id) {
        Categoria c = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        // desfaz o vínculo nos livros antes de remover
        for (Livro livro : c.getLivros()) {
            livro.getCategorias().remove(c);
        }
        categoriaRepository.delete(c);
    }
}
