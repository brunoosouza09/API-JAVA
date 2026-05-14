package com.biblioteca.api.exception;

/**
 * Exceção lançada quando um recurso solicitado (Livro, Autor, Editora,
 * Categoria) não é encontrado no banco de dados.
 *
 * <p>É tratada pelo {@link GlobalExceptionHandler}, que a converte em uma
 * resposta HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Cria a exceção com uma mensagem livre.
     *
     * @param message descrição do erro
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Cria a exceção formatando uma mensagem padrão a partir do nome do
     * recurso e do id que não foi encontrado.
     *
     * @param resource nome do recurso (ex.: "Livro", "Autor")
     * @param id identificador buscado
     */
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " com id " + id + " não encontrado(a)");
    }
}
