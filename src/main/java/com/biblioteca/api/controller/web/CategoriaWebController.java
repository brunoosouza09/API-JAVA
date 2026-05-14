package com.biblioteca.api.controller.web;

import com.biblioteca.api.dto.request.CategoriaRequestDTO;
import com.biblioteca.api.exception.BusinessException;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller MVC para a interface web de Categorias.
 */
@Controller
@RequestMapping("/web/categorias")
public class CategoriaWebController {

    private final CategoriaService service;

    public CategoriaWebController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", service.listar());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new CategoriaRequestDTO());
        }
        return "categorias/lista";
    }

    @PostMapping
    public String criar(@ModelAttribute("form") CategoriaRequestDTO form, RedirectAttributes ra) {
        try {
            service.criar(form);
            ra.addFlashAttribute("msgSucesso", "Categoria criada com sucesso!");
        } catch (BusinessException ex) {
            ra.addFlashAttribute("msgErro", ex.getMessage());
            ra.addFlashAttribute("form", form);
        }
        return "redirect:/web/categorias";
    }

    @PostMapping("/{id}/excluir")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.deletar(id);
            ra.addFlashAttribute("msgSucesso", "Categoria removida!");
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("msgErro", ex.getMessage());
        }
        return "redirect:/web/categorias";
    }
}
