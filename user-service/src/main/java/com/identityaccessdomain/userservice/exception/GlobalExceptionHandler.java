package com.identityaccessdomain.userservice.exception;

import com.identityaccessdomain.userservice.exception.dto.ApiErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private ApiErrorResponse buildError(HttpStatus status, String message,
                                      String path, Map<String, String> fields) {
    return new ApiErrorResponse(
      LocalDateTime.now(),
      status.value(),
      status.getReasonPhrase(),
      message,
      path,
      fields
    );
  }

  // 400 - Erro de validação
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                     HttpServletRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String message = error.getDefaultMessage();
      errors.put(fieldName, message);
    });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(buildError(HttpStatus.BAD_REQUEST, "Erro de validação nos campos", request.getRequestURI(), errors));
  }

  // 400 - Violação de constraints (ex.: @NotBlank em Services)
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                    HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null));
  }

  // 404 - Entidade não encontrada
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFound(EntityNotFoundException ex,
                                                         HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null));
  }

  // 405 - Método HTTP inválido
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
                                                                 HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
      .body(buildError(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não permitido",
        request.getRequestURI(), null));
  }

  // 408 - Timeout
  @ExceptionHandler(SocketTimeoutException.class)
  public ResponseEntity<ApiErrorResponse> handleTimeout(SocketTimeoutException ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
      .body(buildError(HttpStatus.REQUEST_TIMEOUT, "Tempo de requisição excedido",
        request.getRequestURI(), null));
  }

  // 409 - Conflito (ex.: email duplicado)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleConflict(DataIntegrityViolationException ex,
                                                         HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
      .body(buildError(HttpStatus.CONFLICT, "Violação de integridade de dados", request.getRequestURI(), null));
  }

  // 500 - Erros inesperados
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
    log.error("Erro inesperado", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor",
        request.getRequestURI(), null));
  }

}
