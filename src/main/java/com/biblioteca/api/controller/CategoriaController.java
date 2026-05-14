package com.biblioteca.api.controller;

import com.biblioteca.api.dto.request.CategoriaRequestDTO;
import com.biblioteca.api.dto.response.CategoriaResponseDTO;
import com.biblioteca.api.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller REST do recurso Categoria.
 *
 * <p>Expõe os endpoints sob {@code /api/categorias}:
 * <ul>
 *   <li>{@code GET    /api/categorias}       — listar todas</li>
 *   <li>{@code GET    /api/categorias/{id}}  — buscar por id</li>
 *   <li>{@code POST   /api/categorias}       — criar</li>
 *   <li>{@code PUT    /api/categorias/{id}}  — atualizar</li>
 *   <li>{@code DELETE /api/categorias/{id}}  — remover</li>
 * </ul>
 *
 * <p>Delega toda a lógica ao {@link CategoriaService}.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    /**
     * Construtor com injeção do service de categorias.
     */
    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    /**
     * Retorna todas as categorias cadastradas.
     *
     * @return 200 OK com a lista de categorias
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Retorna uma categoria pelo id.
     *
     * @param id identificador da categoria
     * @return 200 OK com a categoria; 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * Cria uma nova categoria.
     *
     * @param dto dados validados de entrada
     * @return 201 Created com Location e corpo criado
     */
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO criada = service.criar(dto);
        return ResponseEntity.created(URI.create("/api/categorias/" + criada.getId())).body(criada);
    }

    /**
     * Atualiza uma categoria existente.
     *
     * @param id identificador da categoria
     * @param dto novos valores (validados)
     * @return 200 OK com a categoria atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    /**
     * Remove uma categoria pelo id.
     *
     * @param id identificador da categoria
     * @return 204 No Content em caso de sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
