// Pacote das classes de configuração.
package com.biblioteca.api.config;

// Classe principal do OpenAPI da biblioteca springdoc.
import io.swagger.v3.oas.models.OpenAPI;
// Dados de contato exibidos no Swagger.
import io.swagger.v3.oas.models.info.Contact;
// Bloco "info" do OpenAPI (título, descrição, versão).
import io.swagger.v3.oas.models.info.Info;
// Tipo de licença mostrado no topo da página.
import io.swagger.v3.oas.models.info.License;
// @Bean marca um método que produz um objeto gerenciado pelo Spring.
import org.springframework.context.annotation.Bean;
// @Configuration marca a classe como contendo definições de beans.
import org.springframework.context.annotation.Configuration;

/*
 * Configuração do OpenAPI/Swagger.
 * Personaliza os metadados (título, descrição, contato) que aparecem
 * na página /swagger-ui.html, fornecida automaticamente pelo springdoc.
 */
@Configuration
public class OpenApiConfig {

    // @Bean: este método produz um bean do tipo OpenAPI que o springdoc usa.
    @Bean
    public OpenAPI bibliotecaApiOpenApi() {
        // Constrói o objeto OpenAPI preenchendo apenas o bloco "info".
        return new OpenAPI()
                .info(new Info()
                        // Título exibido no topo do Swagger UI.
                        .title("Biblioteca API")
                        // Descrição da API.
                        .description("Web API REST para gerenciamento de uma biblioteca (Livros e Editoras)")
                        // Versão da API.
                        .version("1.0.0")
                        // Contato responsável (nome + email).
                        .contact(new Contact().name("Bruno").email("brunogtbruno@gmail.com"))
                        // Licença (qualquer string descritiva).
                        .license(new License().name("Uso acadêmico")));
    }
}
