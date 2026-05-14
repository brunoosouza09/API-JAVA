// Pacote dos controllers MVC.
package com.biblioteca.api.controller.web;

// DTO de entrada da Editora (também usado como model do formulário).
import com.biblioteca.api.dto.request.EditoraRequestDTO;
// Exceções tratadas para exibir erro no JSP.
import com.biblioteca.api.exception.BusinessException;
import com.biblioteca.api.exception.ResourceNotFoundException;
// Service que contém as regras de negócio.
import com.biblioteca.api.service.EditoraService;
// @Controller marca como controller MVC.
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// Anotações usadas nos endpoints.
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// RedirectAttributes para mensagens flash (sucesso/erro após redirect).
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * Controller MVC para a página web de Editoras.
 * Renderiza a tabela com as editoras existentes e um formulário
 * inline para cadastrar/excluir.
 */
@Controller
@RequestMapping("/web/editoras")
public class EditoraWebController {

    // Service de Editora.
    private final EditoraService service;

    // Construtor com injeção do service.
    public EditoraWebController(EditoraService service) {
        this.service = service;
    }

    // GET /web/editoras -> lista todas as editoras.
    @GetMapping
    public String listar(Model model) {
        // Lista todas as editoras.
        model.addAttribute("editoras", service.listar());
        // Se não há um form vindo de redirect, cria um vazio.
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new EditoraRequestDTO());
        }
        // Resolve para /WEB-INF/jsp/editoras/lista.jsp.
        return "editoras/lista";
    }

    // POST /web/editoras -> cria a editora.
    @PostMapping
    public String criar(@ModelAttribute("form") EditoraRequestDTO form, RedirectAttributes ra) {
        try {
            service.criar(form);
            ra.addFlashAttribute("msgSucesso", "Editora criada com sucesso!");
        } catch (BusinessException ex) {
            // Mensagem de erro + manutenção do formulário preenchido.
            ra.addFlashAttribute("msgErro", ex.getMessage());
            ra.addFlashAttribute("form", form);
        }
        // Padrão PRG (Post/Redirect/Get).
        return "redirect:/web/editoras";
    }

    // POST /web/editoras/{id}/excluir -> apaga a editora.
    @PostMapping("/{id}/excluir")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.deletar(id);
            ra.addFlashAttribute("msgSucesso", "Editora removida!");
        } catch (ResourceNotFoundException | BusinessException ex) {
            ra.addFlashAttribute("msgErro", ex.getMessage());
        }
        return "redirect:/web/editoras";
    }
}
