package com.biblioteca.api.controller.web;

import com.biblioteca.api.dto.request.LivroRequestDTO;
import com.biblioteca.api.exception.BusinessException;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.service.AutorService;
import com.biblioteca.api.service.CategoriaService;
import com.biblioteca.api.service.EditoraService;
import com.biblioteca.api.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;

/**
 * Controller MVC para a interface web de Livros.
 *
 * <p>Diferente dos outros entity controllers, o formulário precisa dos
 * selects de editora, autores e categorias — então a listagem também envia
 * essas listas para o JSP.
 */
@Controller
@RequestMapping("/web/livros")
public class LivroWebController {

    private final LivroService livroService;
    private final EditoraService editoraService;
    private final AutorService autorService;
    private final CategoriaService categoriaService;

    public LivroWebController(LivroService livroService,
                              EditoraService editoraService,
                              AutorService autorService,
                              CategoriaService categoriaService) {
        this.livroService = livroService;
        this.editoraService = editoraService;
        this.autorService = autorService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("livros", livroService.listar());
        model.addAttribute("editoras", editoraService.listar());
        model.addAttribute("autores", autorService.listar());
        model.addAttribute("categorias", categoriaService.listar());
        if (!model.containsAttribute("form")) {
            LivroRequestDTO blank = new LivroRequestDTO();
            blank.setAutoresIds(new HashSet<>());
            blank.setCategoriasIds(new HashSet<>());
            model.addAttribute("form", blank);
        }
        return "livros/lista";
    }

    @PostMapping
    public String criar(@ModelAttribute("form") LivroRequestDTO form, RedirectAttributes ra) {
        try {
            livroService.criar(form);
            ra.addFlashAttribute("msgSucesso", "Livro cadastrado com sucesso!");
        } catch (BusinessException | ResourceNotFoundException ex) {
            ra.addFlashAttribute("msgErro", ex.getMessage());
            ra.addFlashAttribute("form", form);
        }
        return "redirect:/web/livros";
    }

    @PostMapping("/{id}/excluir")
    public String deletar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            livroService.deletar(id);
            ra.addFlashAttribute("msgSucesso", "Livro removido!");
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("msgErro", ex.getMessage());
        }
        return "redirect:/web/livros";
    }
}
