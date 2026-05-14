// Pacote das exceções e dos objetos auxiliares de erro.
package com.biblioteca.api.exception;

// @JsonInclude controla quais campos vão para o JSON (aqui: só os não-nulos).
import com.fasterxml.jackson.annotation.JsonInclude;

// LocalDateTime: marca o instante em que o erro ocorreu.
import java.time.LocalDateTime;
// List: usado para a lista de erros de campos (validação).
import java.util.List;

/*
 * Estrutura padronizada de resposta de erro retornada pela API.
 * Usada pelo GlobalExceptionHandler para que todas as respostas de erro
 * (404, 409, 400, 500) tenham o mesmo formato JSON.
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // omite os campos nulos no JSON
public class ErrorResponse {

    // Momento em que o erro foi gerado.
    private LocalDateTime timestamp;
    // Código HTTP (ex.: 404, 409).
    private int status;
    // Nome curto do erro (ex.: "Not Found").
    private String error;
    // Mensagem explicando o erro.
    private String message;
    // Caminho da requisição que gerou o erro (ex.: /api/livros/999).
    private String path;
    // Lista de erros de campo (preenchida só em erros de validação).
    private List<FieldErrorItem> fieldErrors;

    // Construtor padrão: já preenche o timestamp com o instante atual.
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Construtor de conveniência que preenche os principais campos.
    public ErrorResponse(int status, String error, String message, String path) {
        this(); // chama o construtor padrão (preenche timestamp)
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Getters e setters padrão (necessários para a serialização JSON).
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

    /*
     * Classe interna que representa um único erro de validação de campo.
     * Aparece, por exemplo, na resposta 400 quando o usuário envia um JSON
     * com campos inválidos: { "field": "titulo", "message": "Título é obrigatório" }
     */
    public static class FieldErrorItem {
        // Nome do campo que falhou na validação.
        private String field;
        // Mensagem da validação (vem da anotação @NotBlank, @Size, etc.).
        private String message;

        // Construtor vazio (exigido pelo Jackson).
        public FieldErrorItem() {}

        // Construtor com os dois campos preenchidos.
        public FieldErrorItem(String field, String message) {
            this.field = field;
            this.message = message;
        }

        // Getters e setters padrão.
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
