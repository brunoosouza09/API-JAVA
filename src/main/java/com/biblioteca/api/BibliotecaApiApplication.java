package com.biblioteca.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot da Biblioteca API.
 *
 * <p>É o ponto de entrada (método {@code main}) que dispara o boot do Spring,
 * fazendo o scan dos componentes (Controllers, Services, Repositories) deste
 * pacote e subpacotes.
 *
 * <p>A anotação {@code @SpringBootApplication} combina {@code @Configuration},
 * {@code @EnableAutoConfiguration} e {@code @ComponentScan}.
 */
@SpringBootApplication
public class BibliotecaApiApplication {
    /**
     * Inicia a aplicação Spring Boot.
     *
     * @param args argumentos de linha de comando repassados ao Spring
     */
    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApiApplication.class, args);
    }
}
