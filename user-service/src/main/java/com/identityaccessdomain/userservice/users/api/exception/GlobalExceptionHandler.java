package com.identityaccessdomain.userservice.users.api.exception;

import com.identityaccessdomain.userservice.users.api.dto.ApiErrorResponse;
import com.identityaccessdomain.userservice.users.domain.user.exception.EmailAlreadyExistsException;
import com.identityaccessdomain.userservice.users.domain.user.exception.UserNotFoundException;
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

  // 400 - Validation Errors
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                     HttpServletRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String message = error.getDefaultMessage();
      errors.put(fieldName, message);
    });
    log.warn("Erro de validação nos campos: {}", errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(buildError(HttpStatus.BAD_REQUEST, "Erro de validação nos campos", request.getRequestURI(), errors));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                    HttpServletRequest request) {
    log.warn("Violação de restrição: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null));
  }

  // Exceções de domínio
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleUserNotFound(UserNotFoundException ex,
                                                             HttpServletRequest request) {
    log.warn("Usuário não encontrado: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null));
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ApiErrorResponse> handleEmailExists(EmailAlreadyExistsException ex,
                                                            HttpServletRequest request) {
    log.warn("E-mail já cadastrado: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT)
      .body(buildError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), null));
  }

  // JPA EntityNotFound
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFound(EntityNotFoundException ex,
                                                         HttpServletRequest request) {
    log.warn("Entidade não encontrada: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null));
  }

  // Método HTTP não permitido
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
                                                                 HttpServletRequest request) {
    log.warn("Método HTTP não permitido: {}", ex.getMethod());
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
      .body(buildError(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não permitido", request.getRequestURI(), null));
  }

  // Timeout
  @ExceptionHandler(SocketTimeoutException.class)
  public ResponseEntity<ApiErrorResponse> handleTimeout(SocketTimeoutException ex,
                                                        HttpServletRequest request) {
    log.warn("Tempo de requisição excedido: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
      .body(buildError(HttpStatus.REQUEST_TIMEOUT, "Tempo de requisição excedido", request.getRequestURI(), null));
  }

  // Violação de integridade de dados
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleConflict(DataIntegrityViolationException ex,
                                                         HttpServletRequest request) {
    log.warn("Violação de integridade de dados: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT)
      .body(buildError(HttpStatus.CONFLICT, "Violação de integridade de dados", request.getRequestURI(), null));
  }

  // Exceção geral
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
    log.error("Erro inesperado na aplicação", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", request.getRequestURI(), null));
  }

}
