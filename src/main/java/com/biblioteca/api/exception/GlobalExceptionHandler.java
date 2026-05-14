package com.biblioteca.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Tratador global de exceções da API.
 *
 * <p>Centraliza a conversão de exceções (de negócio, validação, integridade
 * de dados e genéricas) em respostas HTTP padronizadas via
 * {@link ErrorResponse}. Assim, os controllers ficam livres de blocos
 * try/catch e a API responde sempre o mesmo formato de erro.
 *
 * <p>A anotação {@code @RestControllerAdvice} faz com que os métodos abaixo
 * sejam aplicados a todos os {@code @RestController} da aplicação.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Mapeia recursos não encontrados para HTTP 404.
     *
     * @param ex exceção lançada pelos services
     * @param req requisição atual, usada para incluir o path no erro
     * @return resposta 404 Not Found com o corpo de erro padronizado
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Mapeia violações de regra de negócio para HTTP 409.
     *
     * @param ex exceção de negócio lançada pelos services
     * @param req requisição atual
     * @return resposta 409 Conflict
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /**
     * Trata falhas de validação de DTOs anotados com {@code @Valid}
     * (Bean Validation), agregando todos os erros por campo na resposta.
     *
     * @param ex exceção lançada pelo Spring quando a validação falha
     * @param req requisição atual
     * @return resposta 400 Bad Request com a lista de campos inválidos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ErrorResponse.FieldErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldErrorItem(fe.getField(), fe.getDefaultMessage()))
                .toList();
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Erro de validação nos campos enviados",
                req.getRequestURI()
        );
        body.setFieldErrors(errors);
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Trata violações de constraints de Bean Validation feitas fora dos
     * DTOs (ex.: em parâmetros de path/query).
     *
     * @param ex exceção contendo as violações
     * @param req requisição atual
     * @return resposta 400 Bad Request
     */
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

    /**
     * Trata violações de integridade do banco (ex.: unique constraint
     * disparada no nível do JDBC) como conflito.
     *
     * @param ex exceção do Spring/JPA
     * @param req requisição atual
     * @return resposta 409 Conflict
     */
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

    /**
     * Trata corpos de requisição malformados (JSON inválido ou tipos
     * incompatíveis com o DTO esperado).
     *
     * @param ex exceção de leitura do payload
     * @param req requisição atual
     * @return resposta 400 Bad Request
     */
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

    /**
     * Fallback para qualquer exceção não tratada explicitamente acima.
     * Evita vazar stack traces para o cliente, retornando 500 padronizado.
     *
     * @param ex exceção genérica capturada
     * @param req requisição atual
     * @return resposta 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Erro interno: " + ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
