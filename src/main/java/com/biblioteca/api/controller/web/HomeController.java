package com.biblioteca.api.controller.web;

import com.biblioteca.api.repository.AutorRepository;
import com.biblioteca.api.repository.CategoriaRepository;
import com.biblioteca.api.repository.EditoraRepository;
import com.biblioteca.api.repository.LivroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller MVC da página inicial (web/UI), em complemento aos REST controllers
 * que servem JSON. Renderiza um dashboard simples com contagem de cada entidade
 * e links para as listagens.
 */
@Controller
public class HomeController {

    private final LivroRepository livroRepo;
    private final AutorRepository autorRepo;
    private final EditoraRepository editoraRepo;
    private final CategoriaRepository categoriaRepo;

    public HomeController(LivroRepository livroRepo, AutorRepository autorRepo,
                          EditoraRepository editoraRepo, CategoriaRepository categoriaRepo) {
        this.livroRepo = livroRepo;
        this.autorRepo = autorRepo;
        this.editoraRepo = editoraRepo;
        this.categoriaRepo = categoriaRepo;
    }

    /**
     * Página inicial: mostra estatísticas básicas (contagens) e navegação.
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalLivros", livroRepo.count());
        model.addAttribute("totalAutores", autorRepo.count());
        model.addAttribute("totalEditoras", editoraRepo.count());
        model.addAttribute("totalCategorias", categoriaRepo.count());
        return "home";
    }
}
