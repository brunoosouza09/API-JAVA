package com.biblioteca.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do OpenAPI/Swagger para documentar a Biblioteca API.
 *
 * <p>Registra um bean {@link OpenAPI} com metadados (título, descrição,
 * versão, contato, licença) que aparecem na interface do Swagger UI,
 * normalmente em {@code /swagger-ui.html} ou {@code /swagger-ui/index.html}.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Cria o bean de configuração principal do OpenAPI com os metadados
     * exibidos na documentação interativa da API.
     *
     * @return objeto {@link OpenAPI} com as informações da Biblioteca API
     */
    @Bean
    public OpenAPI bibliotecaApiOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Biblioteca API")
                        .description("Web API REST para gerenciamento de uma biblioteca (Livros, Autores, Editoras, Categorias)")
                        .version("1.0.0")
                        .contact(new Contact().name("Bruno").email("brunogtbruno@gmail.com"))
                        .license(new License().name("Uso acadêmico")));
    }
}
