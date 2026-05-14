// Pacote das exceções customizadas.
package com.biblioteca.api.exception;

/*
 * Exceção lançada quando uma regra de negócio é violada,
 * por exemplo: tentar cadastrar duas editoras com o mesmo nome,
 * usar um ISBN já existente, ou deletar uma editora que possui livros.
 *
 * É tratada pelo GlobalExceptionHandler como HTTP 409 Conflict.
 */
public class BusinessException extends RuntimeException {
    // Construtor que recebe a mensagem descrevendo o problema.
    public BusinessException(String message) {
        // Repassa a mensagem para a superclasse (RuntimeException).
        super(message);
    }
}
