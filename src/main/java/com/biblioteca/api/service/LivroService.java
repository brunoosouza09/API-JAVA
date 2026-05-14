package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.LivroRequestDTO;
import com.biblioteca.api.dto.response.LivroResponseDTO;
import com.biblioteca.api.exception.BusinessException;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.model.Autor;
import com.biblioteca.api.model.Categoria;
import com.biblioteca.api.model.Editora;
import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.AutorRepository;
import com.biblioteca.api.repository.CategoriaRepository;
import com.biblioteca.api.repository.EditoraRepository;
import com.biblioteca.api.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Camada de serviço do recurso central: Livro.
 *
 * <p>Orquestra os relacionamentos com Editora (N:1), Autores (N:N) e
 * Categorias (N:N), garantindo que as entidades referenciadas existam
 * antes de salvar o livro. Aplica também a regra de ISBN único e fica
 * responsável pela conversão entre DTOs e entidade.
 */
@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final EditoraRepository editoraRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    /**
     * Construtor com injeção dos quatro repositórios necessários para
     * gerenciar o livro e seus relacionamentos.
     */
    public LivroService(LivroRepository livroRepository,
                        EditoraRepository editoraRepository,
                        AutorRepository autorRepository,
                        CategoriaRepository categoriaRepository) {
        this.livroRepository = livroRepository;
        this.editoraRepository = editoraRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Lista todos os livros do acervo.
     *
     * @return lista (possivelmente vazia) com todos os livros
     */
    @Transactional(readOnly = true)
    public List<LivroResponseDTO> listar() {
        return livroRepository.findAll().stream()
                .map(LivroResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Busca um livro pelo id.
     *
     * @param id identificador do livro
     * @return DTO do livro encontrado
     * @throws ResourceNotFoundException se não houver livro com esse id
     */
    @Transactional(readOnly = true)
    public LivroResponseDTO buscarPorId(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        return LivroResponseDTO.fromEntity(livro);
    }

    /**
     * Cria um novo livro, resolvendo editora, autores e categorias pelos
     * ids enviados no DTO. Rejeita ISBN já cadastrado.
     *
     * @param dto dados validados de entrada
     * @return DTO do livro recém-criado
     * @throws BusinessException se o ISBN já existir
     * @throws ResourceNotFoundException se alguma das entidades
     *         referenciadas (editora, autores, categorias) não existir
     */
    @Transactional
    public LivroResponseDTO criar(LivroRequestDTO dto) {
        if (livroRepository.existsByIsbn(dto.getIsbn())) {
            throw new BusinessException("ISBN '" + dto.getIsbn() + "' já cadastrado");
        }
        Livro livro = new Livro();
        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setNumeroPaginas(dto.getNumeroPaginas());
        livro.setPreco(dto.getPreco());
        livro.setEditora(carregarEditora(dto.getEditoraId()));
        livro.setAutores(carregarAutores(dto.getAutoresIds()));
        livro.setCategorias(carregarCategorias(dto.getCategoriasIds()));
        Livro salvo = livroRepository.save(livro);
        return LivroResponseDTO.fromEntity(salvo);
    }

    /**
     * Atualiza um livro existente, substituindo seus campos e
     * relacionamentos pelos valores informados.
     *
     * @param id identificador do livro
     * @param dto novos valores
     * @return DTO do livro atualizado
     * @throws ResourceNotFoundException se o livro ou alguma entidade
     *         referenciada não existir
     * @throws BusinessException se o novo ISBN já estiver em uso por
     *         outro livro
     */
    @Transactional
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        // só rejeita ISBN duplicado quando o ISBN está sendo alterado
        if (!livro.getIsbn().equals(dto.getIsbn()) && livroRepository.existsByIsbn(dto.getIsbn())) {
            throw new BusinessException("ISBN '" + dto.getIsbn() + "' já cadastrado");
        }
        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setNumeroPaginas(dto.getNumeroPaginas());
        livro.setPreco(dto.getPreco());
        livro.setEditora(carregarEditora(dto.getEditoraId()));
        livro.setAutores(carregarAutores(dto.getAutoresIds()));
        livro.setCategorias(carregarCategorias(dto.getCategoriasIds()));
        return LivroResponseDTO.fromEntity(livro);
    }

    /**
     * Remove um livro pelo id. As linhas das tabelas de junção
     * ({@code livro_autor}, {@code livro_categoria}) são removidas
     * automaticamente pelo JPA.
     *
     * @param id identificador do livro
     * @throws ResourceNotFoundException se o livro não existir
     */
    @Transactional
    public void deletar(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        livroRepository.delete(livro);
    }

    /**
     * Carrega a editora pelo id ou falha se não existir.
     *
     * @param id id da editora referenciada
     * @return entidade {@link Editora} carregada
     * @throws ResourceNotFoundException se a editora não existir
     */
    private Editora carregarEditora(Long id) {
        return editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
    }

    /**
     * Carrega o conjunto de autores pelos ids, falhando se algum não
     * for encontrado (todos os ids enviados precisam existir).
     *
     * @param ids ids dos autores
     * @return conjunto de entidades {@link Autor} carregadas
     * @throws ResourceNotFoundException se qualquer id não corresponder
     *         a um autor existente
     */
    private Set<Autor> carregarAutores(Set<Long> ids) {
        List<Autor> encontrados = autorRepository.findAllById(ids);
        if (encontrados.size() != ids.size()) {
            throw new ResourceNotFoundException("Um ou mais autores não foram encontrados");
        }
        return new HashSet<>(encontrados);
    }

    /**
     * Carrega o conjunto de categorias pelos ids, falhando se alguma não
     * for encontrada (todos os ids enviados precisam existir).
     *
     * @param ids ids das categorias
     * @return conjunto de entidades {@link Categoria} carregadas
     * @throws ResourceNotFoundException se qualquer id não corresponder
     *         a uma categoria existente
     */
    private Set<Categoria> carregarCategorias(Set<Long> ids) {
        List<Categoria> encontradas = categoriaRepository.findAllById(ids);
        if (encontradas.size() != ids.size()) {
            throw new ResourceNotFoundException("Uma ou mais categorias não foram encontradas");
        }
        return new HashSet<>(encontradas);
    }
}
