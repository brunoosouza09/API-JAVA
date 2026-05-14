// Pacote dos controllers MVC (que renderizam páginas HTML usando JSP).
package com.biblioteca.api.controller.web;

// Repositórios usados para contar os registros do banco.
import com.biblioteca.api.repository.EditoraRepository;
import com.biblioteca.api.repository.LivroRepository;
// @Controller marca a classe como controller MVC (devolve nomes de view, não JSON).
import org.springframework.stereotype.Controller;
// Model carrega os atributos que serão passados para a view (JSP).
import org.springframework.ui.Model;
// @GetMapping mapeia uma URL HTTP GET para um método.
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Controller MVC da página inicial.
 * Mostra um pequeno dashboard com a contagem de livros e editoras
 * e links para as listagens.
 */
@Controller
public class HomeController {

    // Repositórios usados só para contar (count()).
    private final LivroRepository livroRepo;
    private final EditoraRepository editoraRepo;

    // Construtor com injeção dos dois repositórios.
    public HomeController(LivroRepository livroRepo, EditoraRepository editoraRepo) {
        this.livroRepo = livroRepo;
        this.editoraRepo = editoraRepo;
    }

    // GET / -> renderiza a página inicial.
    @GetMapping("/")
    public String home(Model model) {
        // Adiciona o total de livros como atributo do model (disponível como ${totalLivros} no JSP).
        model.addAttribute("totalLivros", livroRepo.count());
        // Adiciona o total de editoras.
        model.addAttribute("totalEditoras", editoraRepo.count());
        // Retorna o nome lógico da view; o Spring resolve para /WEB-INF/jsp/home.jsp.
        return "home";
    }
}
