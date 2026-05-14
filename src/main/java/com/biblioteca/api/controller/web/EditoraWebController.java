package com.biblioteca.api.controller.web;

import com.biblioteca.api.dto.request.EditoraRequestDTO;
import com.biblioteca.api.exception.BusinessException;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.service.EditoraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller MVC para a interface web de Editoras.
 *
 * <p>Roteia em {@code /web/editoras}. Renderiza listagem + formulário inline
 * para criar e botões para deletar.
 */
@Controller
@RequestMapping("/web/editoras")
public class EditoraWebController {

    private final EditoraService service;

    public EditoraWebController(EditoraService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("editoras", service.listar());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new EditoraRequestDTO());
        }
        return "editoras/lista";
    }

    @PostMapping
    public String criar(@ModelAttribute("form") EditoraRequestDTO form, RedirectAttributes ra) {
        try {
            service.criar(form);
            ra.addFlashAttribute("msgSucesso", "Editora criada com sucesso!");
        } catch (BusinessException ex) {
            ra.addFlashAttribute("msgErro", ex.getMessage());
            ra.addFlashAttribute("form", form);
        }
        return "redirect:/web/editoras";
    }

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
