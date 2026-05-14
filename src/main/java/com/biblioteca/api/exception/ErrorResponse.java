package com.biblioteca.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Estrutura padronizada de resposta de erro retornada pela API.
 *
 * <p>Usada pelo {@link GlobalExceptionHandler} para padronizar o JSON de
 * erro (timestamp, status, mensagem, caminho da requisição e, opcionalmente,
 * a lista de erros de validação por campo).
 *
 * <p>A anotação {@code @JsonInclude(NON_NULL)} omite campos nulos do JSON
 * (por exemplo, {@code fieldErrors} só aparece em erros de validação).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldErrorItem> fieldErrors;

    /**
     * Construtor padrão que já inicializa o {@code timestamp} com o
     * instante atual.
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Construtor de conveniência que preenche os campos principais
     * da resposta de erro.
     *
     * @param status código HTTP (ex.: 404, 409)
     * @param error nome curto do erro (ex.: "Not Found")
     * @param message mensagem explicativa
     * @param path caminho da requisição que originou o erro
     */
    public ErrorResponse(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public List<FieldErrorItem> getFieldErrors() { return fieldErrors; }
    public void setFieldErrors(List<FieldErrorItem> fieldErrors) { this.fieldErrors = fieldErrors; }

    /**
     * Item que representa um erro de validação em um campo específico do
     * payload de entrada (usado em respostas 400 Bad Request).
     */
    public static class FieldErrorItem {
        private String field;
        private String message;

        public FieldErrorItem() {}

        /**
         * Cria um item de erro de campo.
         *
         * @param field nome do campo que falhou na validação
         * @param message mensagem da validação (ex.: "Nome é obrigatório")
         */
        public FieldErrorItem(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
