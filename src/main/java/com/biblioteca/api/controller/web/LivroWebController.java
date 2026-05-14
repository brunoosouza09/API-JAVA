// Pacote dos controllers MVC (páginas web).
package com.biblioteca.api.controller.web;

// DTO de entrada (também usado como model do formulário do JSP).
import com.biblioteca.api.dto.request.LivroRequestDTO;
// Exceções tratadas para exibir mensagem de erro amigável no JSP.
import com.biblioteca.api.exception.BusinessException;
import com.biblioteca.api.exception.ResourceNotFoundException;
// Services usados para listar/criar/deletar livros e listar editoras.
import com.biblioteca.api.service.EditoraService;
import com.biblioteca.api.service.LivroService;
// @Controller marca a classe como controller MVC.
import org.springframework.stereotype.Controller;
// Model carrega atributos para o JSP.
import org.springframework.ui.Model;
// Anotações Spring usadas nos métodos.
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// RedirectAttributes permite enviar mensagens "flash" após um redirect (alerta de sucesso/erro).
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * Controller MVC para a página web de Livros.
 * Renderiza a lista de livros com um formulário inline para criar
 * e botões para excluir cada um.
 */
@Controller
@RequestMapping("/web/livros")
public class LivroWebController {

    // Service de Livro (regras + acesso a dados).
    private final LivroService livroService;
    // Service de Editora (para preencher o select de editoras no formulário).
    private final EditoraService editoraService;

    // Construtor com injeção dos services.
    public LivroWebController(LivroService livroService, EditoraService editoraService) {
        this.livroService = livroService;
        this.editoraService = editoraService;
    }

    // GET /web/livros -> renderiza a lista.
    @GetMapping
    public String listar(Model model) {
        // Lista todos os livros (usados para renderizar os cards).
        model.addAttribute("livros", livroService.listar());
        // Lista todas as editoras (usadas no select do formulário).
        model.addAttribute("editoras", editoraService.listar());
        // Se ainda não há um "form" no model (vindo de um redirect), cria um vazio.
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new LivroRequestDTO());
        }
        // Resolve para /WEB-INF/jsp/livros/lista.jsp.
        return "livros/lista";
    }

    // POST /web/livros -> recebe o formulário e cria o livro.
    @PostMapping
    public String criar(@ModelAttribute("form") LivroRequestDTO form, RedirectAttributes ra) {
        try {
            // Tenta criar via service.
            livroService.criar(form);
            // Mensagem flash de sucesso (some após o reload).
            ra.addFlashAttribute("msgSucesso", "Livro cadastrado com sucesso!");
        } catch (BusinessException | ResourceNotFoundException ex) {
            // Em caso de erro, devolve a mensagem e mantém o formulário preenchido.
            ra.addFlashAttribute("msgErro", ex.getMessage());
            ra.addFlashAttribute("form", form);
        }
        // Redireciona para a listagem (padrão PRG = Post/Redirect/Get).
        return "redirect:/web/livros";
    }

    // POST /web/livros/{id}/excluir -> deleta o livro.
    @PostMapping("/{id}/excluir")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            livroService.deletar(id);
            ra.addFlashAttribute("msgSucesso", "Livro removido!");
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("msgErro", ex.getMessage());
        }
        // Redireciona para a listagem.
        return "redirect:/web/livros";
    }
}
