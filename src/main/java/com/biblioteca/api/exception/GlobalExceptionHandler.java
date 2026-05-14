// Pacote das exceções e do handler global.
package com.biblioteca.api.exception;

// HttpServletRequest é usado para pegar a URI da requisição que gerou o erro.
import jakarta.servlet.http.HttpServletRequest;
// Exceção do Bean Validation quando feita fora dos DTOs (ex.: em parâmetros).
import jakarta.validation.ConstraintViolationException;
// Exceção do Spring/JPA por violação de integridade no banco (ex.: unique).
import org.springframework.dao.DataIntegrityViolationException;
// HttpStatus contém os códigos HTTP (404, 409, 400, 500...).
import org.springframework.http.HttpStatus;
// ResponseEntity permite controlar status, headers e corpo da resposta.
import org.springframework.http.ResponseEntity;
// Exceção lançada pelo Spring quando o JSON da requisição está mal formado.
import org.springframework.http.converter.HttpMessageNotReadableException;
// Exceção lançada quando o Bean Validation falha em um DTO anotado com @Valid.
import org.springframework.web.bind.MethodArgumentNotValidException;
// @ExceptionHandler marca um método que trata um tipo de exceção.
import org.springframework.web.bind.annotation.ExceptionHandler;
// @RestControllerAdvice aplica os handlers a todos os @RestController da aplicação.
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/*
 * Tratador global de exceções da API.
 * Centraliza a conversão de exceções (de negócio, validação, banco, etc.)
 * em respostas HTTP padronizadas usando o objeto ErrorResponse.
 * Assim, os controllers não precisam usar try/catch — basta deixar a
 * exceção subir que este handler converte em resposta amigável.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Trata ResourceNotFoundException -> 404 Not Found.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        // Monta o corpo do erro com os dados-padrão.
        ErrorResponse body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(), // 404
                "Not Found",
                ex.getMessage(),
                req.getRequestURI()
        );
        // Devolve com status 404.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // Trata BusinessException -> 409 Conflict.
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.CONFLICT.value(), // 409
                "Conflict",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // Trata erro de validação dos DTOs (@Valid) -> 400 Bad Request com lista de campos.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        // Extrai cada erro de campo e converte para o nosso FieldErrorItem.
        List<ErrorResponse.FieldErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldErrorItem(fe.getField(), fe.getDefaultMessage()))
                .toList();
        // Monta o corpo do erro principal.
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), // 400
                "Bad Request",
                "Erro de validação nos campos enviados",
                req.getRequestURI()
        );
        // Anexa a lista de erros de campo.
        body.setFieldErrors(errors);
        return ResponseEntity.badRequest().body(body);
    }

    // Trata ConstraintViolationException (validação fora de DTOs) -> 400.
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.badRequest().body(body);
    }

    // Trata violação de integridade do banco (ex.: unique disparado no JDBC) -> 409.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "Violação de integridade de dados",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // Trata JSON malformado / tipos errados no corpo -> 400.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "JSON inválido ou mal formatado",
                req.getRequestURI()
        );
        return ResponseEntity.badRequest().body(body);
    }

    // Handler genérico (fallback) para qualquer outra exceção -> 500.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
                "Internal Server Error",
                "Erro interno: " + ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
