// Pacote raiz da aplicação.
package com.biblioteca.api;

// Classe utilitária do Spring Boot que sobe a aplicação (cria contexto, servidor embutido, etc.).
import org.springframework.boot.SpringApplication;
// Anotação que liga várias coisas: @Configuration + @EnableAutoConfiguration + @ComponentScan.
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Classe principal da aplicação Spring Boot.
 * É o ponto de entrada (main) que dispara o boot do framework.
 * Por padrão, o @SpringBootApplication faz o "component scan" deste pacote
 * e dos subpacotes — então qualquer @Controller, @Service, @Repository
 * abaixo de com.biblioteca.api é detectado automaticamente.
 */
@SpringBootApplication
public class BibliotecaApiApplication {
    // Método main: ponto de entrada da JVM.
    public static void main(String[] args) {
        // SpringApplication.run sobe a aplicação inteira: cria o contexto,
        // inicia o Tomcat embarcado, registra controllers, etc.
        SpringApplication.run(BibliotecaApiApplication.class, args);
    }
}
