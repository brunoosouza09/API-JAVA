package com.biblioteca.api.controller;

import com.biblioteca.api.dto.request.AutorRequestDTO;
import com.biblioteca.api.dto.response.AutorResponseDTO;
import com.biblioteca.api.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller REST do recurso Autor.
 *
 * <p>Expõe os endpoints sob {@code /api/autores}:
 * <ul>
 *   <li>{@code GET    /api/autores}       — listar todos</li>
 *   <li>{@code GET    /api/autores/{id}}  — buscar por id</li>
 *   <li>{@code POST   /api/autores}       — criar</li>
 *   <li>{@code PUT    /api/autores/{id}}  — atualizar</li>
 *   <li>{@code DELETE /api/autores/{id}}  — remover</li>
 * </ul>
 *
 * <p>Delega toda a lógica ao {@link AutorService}.
 */
@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService service;

    /**
     * Construtor com injeção do service de autores.
     */
    public AutorController(AutorService service) {
        this.service = service;
    }

    /**
     * Retorna todos os autores cadastrados.
     *
     * @return 200 OK com a lista de autores
     */
    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Retorna um autor pelo id.
     *
     * @param id identificador do autor
     * @return 200 OK com o autor; 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * Cria um novo autor.
     *
     * @param dto dados validados de entrada
     * @return 201 Created com Location e corpo criado
     */
    @PostMapping
    public ResponseEntity<AutorResponseDTO> criar(@Valid @RequestBody AutorRequestDTO dto) {
        AutorResponseDTO criado = service.criar(dto);
        return ResponseEntity.created(URI.create("/api/autores/" + criado.getId())).body(criado);
    }

    /**
     * Atualiza um autor existente.
     *
     * @param id identificador do autor
     * @param dto novos valores (validados)
     * @return 200 OK com o autor atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AutorRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    /**
     * Remove um autor pelo id.
     *
     * @param id identificador do autor
     * @return 204 No Content em caso de sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
