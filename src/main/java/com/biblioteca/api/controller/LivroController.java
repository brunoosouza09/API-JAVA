package com.biblioteca.api.controller;

import com.biblioteca.api.dto.request.LivroRequestDTO;
import com.biblioteca.api.dto.response.LivroResponseDTO;
import com.biblioteca.api.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller REST do recurso central: Livro.
 *
 * <p>Expõe os endpoints sob {@code /api/livros}:
 * <ul>
 *   <li>{@code GET    /api/livros}       — listar todos</li>
 *   <li>{@code GET    /api/livros/{id}}  — buscar por id</li>
 *   <li>{@code POST   /api/livros}       — criar</li>
 *   <li>{@code PUT    /api/livros/{id}}  — atualizar</li>
 *   <li>{@code DELETE /api/livros/{id}}  — remover</li>
 * </ul>
 *
 * <p>O payload de entrada usa ids para referenciar editora, autores e
 * categorias; a resolução para entidades reais é feita pelo
 * {@link LivroService}.
 */
@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService service;

    /**
     * Construtor com injeção do service de livros.
     */
    public LivroController(LivroService service) {
        this.service = service;
    }

    /**
     * Retorna todos os livros do acervo.
     *
     * @return 200 OK com a lista de livros
     */
    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Retorna um livro pelo id.
     *
     * @param id identificador do livro
     * @return 200 OK com o livro; 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * Cria um novo livro a partir dos dados validados.
     *
     * @param dto dados do livro, incluindo ids de editora/autores/categorias
     * @return 201 Created com Location e corpo criado
     */
    @PostMapping
    public ResponseEntity<LivroResponseDTO> criar(@Valid @RequestBody LivroRequestDTO dto) {
        LivroResponseDTO criado = service.criar(dto);
        return ResponseEntity.created(URI.create("/api/livros/" + criado.getId())).body(criado);
    }

    /**
     * Atualiza um livro existente.
     *
     * @param id identificador do livro
     * @param dto novos valores (validados)
     * @return 200 OK com o livro atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LivroRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    /**
     * Remove um livro pelo id.
     *
     * @param id identificador do livro
     * @return 204 No Content em caso de sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
