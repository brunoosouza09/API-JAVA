package com.biblioteca.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

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
