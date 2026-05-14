// Pacote dos controllers REST.
package com.biblioteca.api.controller;

// DTOs de entrada e saída de Editora.
import com.biblioteca.api.dto.request.EditoraRequestDTO;
import com.biblioteca.api.dto.response.EditoraResponseDTO;
// Service que contém as regras de negócio.
import com.biblioteca.api.service.EditoraService;
// @Valid dispara o Bean Validation no DTO.
import jakarta.validation.Valid;
// ResponseEntity controla status HTTP e cabeçalhos da resposta.
import org.springframework.http.ResponseEntity;
// Anotações REST do Spring (@RestController, @RequestMapping, etc.).
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/*
 * Controller REST do recurso Editora.
 * Expõe os endpoints REST em /api/editoras (GET, POST, PUT, DELETE).
 */
@RestController
@RequestMapping("/api/editoras")
public class EditoraController {

    // Service de Editora (injeção via construtor).
    private final EditoraService service;

    // Construtor com injeção do service.
    public EditoraController(EditoraService service) {
        this.service = service;
    }

    // GET /api/editoras -> lista todas.
    @GetMapping
    public ResponseEntity<List<EditoraResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // GET /api/editoras/{id} -> busca pelo id.
    @GetMapping("/{id}")
    public ResponseEntity<EditoraResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // POST /api/editoras -> cria nova editora.
    @PostMapping
    public ResponseEntity<EditoraResponseDTO> criar(@Valid @RequestBody EditoraRequestDTO dto) {
        EditoraResponseDTO criada = service.criar(dto);
        // 201 Created com o header Location apontando para o novo recurso.
        return ResponseEntity.created(URI.create("/api/editoras/" + criada.getId())).body(criada);
    }

    // PUT /api/editoras/{id} -> atualiza editora existente.
    @PutMapping("/{id}")
    public ResponseEntity<EditoraResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody EditoraRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    // DELETE /api/editoras/{id} -> remove a editora.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        // 204 No Content (operação OK sem corpo de resposta).
        return ResponseEntity.noContent().build();
    }
}
