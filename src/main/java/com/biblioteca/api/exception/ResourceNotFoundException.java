// Pacote das exceções customizadas.
package com.biblioteca.api.exception;

/*
 * Exceção lançada quando um recurso (Livro ou Editora) não é encontrado.
 * É tratada pelo GlobalExceptionHandler como HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {
    // Construtor com mensagem genérica (qualquer texto).
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Construtor que monta uma mensagem padrão "X com id N não encontrado(a)".
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " com id " + id + " não encontrado(a)");
    }
}
