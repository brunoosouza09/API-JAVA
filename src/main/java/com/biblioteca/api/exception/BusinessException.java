package com.biblioteca.api.exception;

/**
 * Exceção usada para sinalizar violações de regras de negócio, como
 * duplicidade de chave única (nome de editora, ISBN) ou tentativas de
 * exclusão bloqueadas por dependências.
 *
 * <p>É tratada pelo {@link GlobalExceptionHandler}, que a converte em uma
 * resposta HTTP 409 Conflict.
 */
public class BusinessException extends RuntimeException {
    /**
     * Cria a exceção com a mensagem que explica a regra violada.
     *
     * @param message descrição da regra de negócio violada
     */
    public BusinessException(String message) {
        super(message);
    }
}
