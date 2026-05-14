// Pacote dos controllers REST.
package com.biblioteca.api.controller;

// DTO de entrada (recebido no corpo do POST/PUT).
import com.biblioteca.api.dto.request.LivroRequestDTO;
// DTO de saída (devolvido nas respostas).
import com.biblioteca.api.dto.response.LivroResponseDTO;
// Service que contém as regras de negócio.
import com.biblioteca.api.service.LivroService;
// @Valid dispara a validação das anotações do Bean Validation no DTO.
import jakarta.validation.Valid;
// ResponseEntity permite controlar status HTTP e cabeçalhos da resposta.
import org.springframework.http.ResponseEntity;
// Anotações REST do Spring (@RestController, @RequestMapping, @GetMapping, etc.).
import org.springframework.web.bind.annotation.*;

// URI usado para montar o header Location em respostas 201 Created.
import java.net.URI;
import java.util.List;

/*
 * Controller REST do recurso Livro.
 * Expõe os endpoints REST em /api/livros (GET, POST, PUT, DELETE).
 * Não contém regras de negócio: apenas adapta HTTP <-> Service.
 */
@RestController
// @RequestMapping define a URL base que todos os endpoints da classe vão usar.
@RequestMapping("/api/livros")
public class LivroController {

    // Injeção do service (final = não pode ser reatribuído depois).
    private final LivroService service;

    // Construtor com injeção do service (Spring fornece a instância).
    public LivroController(LivroService service) {
        this.service = service;
    }

    // GET /api/livros -> lista todos os livros.
    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listar() {
        // Devolve 200 OK com a lista de livros no corpo.
        return ResponseEntity.ok(service.listar());
    }

    // GET /api/livros/{id} -> busca um livro pelo id.
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscar(@PathVariable Long id) {
        // 200 OK com o livro. Se não existir, o service lança 404 (tratado pelo handler global).
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // POST /api/livros -> cria um novo livro.
    @PostMapping
    public ResponseEntity<LivroResponseDTO> criar(@Valid @RequestBody LivroRequestDTO dto) {
        // Service faz as validações de negócio e persiste.
        LivroResponseDTO criado = service.criar(dto);
        // Retorna 201 Created com o header Location apontando para o novo recurso.
        return ResponseEntity.created(URI.create("/api/livros/" + criado.getId())).body(criado);
    }

    // PUT /api/livros/{id} -> atualiza um livro.
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LivroRequestDTO dto) {
        // 200 OK com o livro atualizado.
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    // DELETE /api/livros/{id} -> remove um livro.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        // 204 No Content (deletado, sem corpo na resposta).
        return ResponseEntity.noContent().build();
    }
}
