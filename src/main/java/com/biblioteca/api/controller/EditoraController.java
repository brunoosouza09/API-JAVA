package com.biblioteca.api.controller;

import com.biblioteca.api.dto.request.EditoraRequestDTO;
import com.biblioteca.api.dto.response.EditoraResponseDTO;
import com.biblioteca.api.service.EditoraService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller REST do recurso Editora.
 *
 * <p>Expõe os endpoints sob {@code /api/editoras}:
 * <ul>
 *   <li>{@code GET    /api/editoras}       — listar todas</li>
 *   <li>{@code GET    /api/editoras/{id}}  — buscar por id</li>
 *   <li>{@code POST   /api/editoras}       — criar</li>
 *   <li>{@code PUT    /api/editoras/{id}}  — atualizar</li>
 *   <li>{@code DELETE /api/editoras/{id}}  — remover</li>
 * </ul>
 *
 * <p>Não contém regras de negócio: delega toda a lógica ao
 * {@link EditoraService} e apenas adapta a entrada/saída HTTP.
 */
@RestController
@RequestMapping("/api/editoras")
public class EditoraController {

    private final EditoraService service;

    /**
     * Construtor com injeção do service de editoras.
     */
    public EditoraController(EditoraService service) {
        this.service = service;
    }

    /**
     * Retorna todas as editoras cadastradas.
     *
     * @return 200 OK com a lista de editoras
     */
    @GetMapping
    public ResponseEntity<List<EditoraResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Retorna uma editora pelo id.
     *
     * @param id identificador da editora
     * @return 200 OK com a editora; 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<EditoraResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * Cria uma nova editora.
     *
     * @param dto dados da editora (validados pelo {@code @Valid})
     * @return 201 Created com o cabeçalho Location e o corpo criado
     */
    @PostMapping
    public ResponseEntity<EditoraResponseDTO> criar(@Valid @RequestBody EditoraRequestDTO dto) {
        EditoraResponseDTO criada = service.criar(dto);
        return ResponseEntity.created(URI.create("/api/editoras/" + criada.getId())).body(criada);
    }

    /**
     * Atualiza uma editora existente.
     *
     * @param id identificador da editora
     * @param dto novos valores (validados)
     * @return 200 OK com a editora atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<EditoraResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody EditoraRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    /**
     * Remove uma editora pelo id.
     *
     * @param id identificador da editora
     * @return 204 No Content em caso de sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
