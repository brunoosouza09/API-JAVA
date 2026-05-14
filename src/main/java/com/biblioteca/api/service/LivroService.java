// Pacote da camada de serviço (regras de negócio).
package com.biblioteca.api.service;

// Importa o DTO de entrada.
import com.biblioteca.api.dto.request.LivroRequestDTO;
// Importa o DTO de saída.
import com.biblioteca.api.dto.response.LivroResponseDTO;
// Exceção lançada quando uma regra de negócio é violada (ex.: ISBN duplicado).
import com.biblioteca.api.exception.BusinessException;
// Exceção lançada quando um recurso não é encontrado.
import com.biblioteca.api.exception.ResourceNotFoundException;
// Entidade Editora (para preencher o relacionamento N:1).
import com.biblioteca.api.model.Editora;
// Entidade Livro.
import com.biblioteca.api.model.Livro;
// Repositórios para acessar o banco.
import com.biblioteca.api.repository.EditoraRepository;
import com.biblioteca.api.repository.LivroRepository;
// @Service marca a classe como componente da camada de serviço (gerenciada pelo Spring).
import org.springframework.stereotype.Service;

// java.util.List é usado para retornar listas de DTOs.
import java.util.List;

/*
 * Camada de serviço de Livro: contém as regras de negócio,
 * faz a conversão entre DTO e entidade e usa o repositório para
 * persistir/consultar dados no banco.
 */
@Service
public class LivroService {

    // Repositório de Livro (CRUD da entidade Livro).
    private final LivroRepository livroRepository;
    // Repositório de Editora (usado para resolver o id da editora ao criar/atualizar um livro).
    private final EditoraRepository editoraRepository;

    // Construtor com injeção de dependências (o Spring fornece os repositórios automaticamente).
    public LivroService(LivroRepository livroRepository, EditoraRepository editoraRepository) {
        this.livroRepository = livroRepository;
        this.editoraRepository = editoraRepository;
    }

    // Lista todos os livros do acervo, convertidos para DTO.
    public List<LivroResponseDTO> listar() {
        // findAll() devolve todas as entidades; stream().map() converte cada uma para DTO.
        return livroRepository.findAll().stream()
                .map(LivroResponseDTO::fromEntity)
                .toList();
    }

    // Busca um livro pelo id. Lança 404 se não existir.
    public LivroResponseDTO buscarPorId(Long id) {
        // findById devolve Optional; se vazio, lança ResourceNotFoundException.
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        // Converte a entidade encontrada em DTO de resposta.
        return LivroResponseDTO.fromEntity(livro);
    }

    // Cria um novo livro a partir do DTO de entrada.
    public LivroResponseDTO criar(LivroRequestDTO dto) {
        // Regra: não pode existir outro livro com o mesmo ISBN.
        if (livroRepository.existsByIsbn(dto.getIsbn())) {
            throw new BusinessException("ISBN '" + dto.getIsbn() + "' já cadastrado");
        }
        // Cria uma nova entidade e copia os campos do DTO.
        Livro livro = new Livro();
        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setNumeroPaginas(dto.getNumeroPaginas());
        livro.setPreco(dto.getPreco());
        // Busca a editora pelo id (ou lança 404 se não existir).
        livro.setEditora(carregarEditora(dto.getEditoraId()));
        // Salva no banco; save() devolve a entidade já com o id gerado.
        Livro salvo = livroRepository.save(livro);
        // Converte para DTO de saída.
        return LivroResponseDTO.fromEntity(salvo);
    }

    // Atualiza um livro existente.
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        // Busca o livro; 404 se não existir.
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        // Só verifica duplicidade de ISBN se ele realmente mudou.
        if (!livro.getIsbn().equals(dto.getIsbn()) && livroRepository.existsByIsbn(dto.getIsbn())) {
            throw new BusinessException("ISBN '" + dto.getIsbn() + "' já cadastrado");
        }
        // Atualiza cada campo da entidade com os valores do DTO.
        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setNumeroPaginas(dto.getNumeroPaginas());
        livro.setPreco(dto.getPreco());
        livro.setEditora(carregarEditora(dto.getEditoraId()));
        // Como a entidade já está vinculada ao contexto de persistência,
        // basta salvar para garantir o flush e devolver o estado atualizado.
        Livro atualizado = livroRepository.save(livro);
        return LivroResponseDTO.fromEntity(atualizado);
    }

    // Remove um livro pelo id.
    public void deletar(Long id) {
        // Busca o livro antes de deletar; 404 se não existir.
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        // Apaga a entidade do banco.
        livroRepository.delete(livro);
    }

    // Método auxiliar: carrega a editora pelo id ou lança 404.
    private Editora carregarEditora(Long id) {
        return editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
    }
}
