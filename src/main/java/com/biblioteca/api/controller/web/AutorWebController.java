package com.biblioteca.api.controller.web;

import com.biblioteca.api.dto.request.AutorRequestDTO;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.service.AutorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller MVC para a interface web de Autores.
 */
@Controller
@RequestMapping("/web/autores")
public class AutorWebController {

    private final AutorService service;

    public AutorWebController(AutorService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("autores", service.listar());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new AutorRequestDTO());
        }
        return "autores/lista";
    }

    @PostMapping
    public String criar(@ModelAttribute("form") AutorRequestDTO form, RedirectAttributes ra) {
        try {
            service.criar(form);
            ra.addFlashAttribute("msgSucesso", "Autor criado com sucesso!");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("msgErro", ex.getMessage());
            ra.addFlashAttribute("form", form);
        }
        return "redirect:/web/autores";
    }

    @PostMapping("/{id}/excluir")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.deletar(id);
            ra.addFlashAttribute("msgSucesso", "Autor removido!");
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("msgErro", ex.getMessage());
        }
        return "redirect:/web/autores";
    }
}
