package com.identityaccessdomain.identityprofiles.exception.handler;

import com.identityaccessdomain.identityprofiles.dto.response.ErrorResponse;
import com.identityaccessdomain.identityprofiles.exception.ConflictException;
import com.identityaccessdomain.identityprofiles.exception.InternalServerErrorException;
import com.identityaccessdomain.identityprofiles.exception.ResourceNotFoundException;
import com.identityaccessdomain.identityprofiles.exception.UnprocessableEntityException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 03/10/2025
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  public static final String VALIDATION_ERROR = "Erro de validação: {}";

  private ErrorResponse buildErrorResponse(HttpStatus status, String message, WebRequest request) {
    return ErrorResponse.builder()
      .status(status.value())
      .error(status.getReasonPhrase())
      .message(message)
      .path(((ServletWebRequest) request).getRequest().getRequestURI())
      .timestamp(LocalDateTime.now())
      .build();
  }

  private ResponseEntity<Object> buildResponse(String message, Exception ex) {
    log.error("Exceção capturada: status={}, mensagem={}, detalhe={}",
      HttpStatus.INTERNAL_SERVER_ERROR, message, ex.getMessage(), ex);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
      Map.of(
        "timestamp", LocalDateTime.now(),
        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        "message", message
      )
    );
  }

  // 400 - Validações de @Valid ou @Validated
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
      .map(err -> err.getField() + ": " + err.getDefaultMessage())
      .collect(Collectors.joining(", "));
    log.warn(VALIDATION_ERROR, msg);
    return ResponseEntity.badRequest().body(buildErrorResponse(HttpStatus.BAD_REQUEST, msg, request));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                 WebRequest request) {
    String msg = ex.getConstraintViolations().stream()
      .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
      .collect(Collectors.joining(", "));
    log.warn(VALIDATION_ERROR, msg);
    return ResponseEntity.badRequest().body(buildErrorResponse(HttpStatus.BAD_REQUEST, msg, request));
  }

  // 404 Recurso não encontrado
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
    log.warn("Recurso não encontrado: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request));
  }

  // 405 (Metodo HTTP não suportado)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
                                                              WebRequest request) {
    String msg = "Método não permitido: " + ex.getMethod();
    log.warn(msg);
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
      .body(buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, msg, request));
  }

  // 408 (tempo de requisição esgotado) → usado geralmente em proxy, mas pode ser manual
  @ExceptionHandler(TimeoutException.class)
  public ResponseEntity<ErrorResponse> handleTimeout(TimeoutException ex, WebRequest request) {
    log.error("Timeout: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
      .body(buildErrorResponse(HttpStatus.REQUEST_TIMEOUT, ex.getMessage(), request));
  }

  // 409 Recurso já existe
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, WebRequest request) {
    log.warn("Conflito de dados: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT)
      .body(buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request));
  }

  // 422
  @ExceptionHandler(UnprocessableEntityException.class)
  public ResponseEntity<ErrorResponse> handleUnprocessableEntity(UnprocessableEntityException ex,
                                                                 WebRequest request) {
    log.warn("Entidade não processável: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
      .body(buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request));
  }

  // 500 INTERNAL_SERVER_ERROR
  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<Object> handleInternalServerError(InternalServerErrorException ex) {
    return buildResponse(ex.getMessage(), ex);
  }

  // 500 - fallback
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, WebRequest request) {
    log.error("Erro inesperado no servidor: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor", request));
  }

  // 503 - indisponibilidade (exemplo)
  @ExceptionHandler({IllegalStateException.class})
  public ResponseEntity<ErrorResponse> handleServiceUnavailable(IllegalStateException ex, WebRequest request) {
    log.error("Serviço indisponível: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
      .body(buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), request));
  }

}
