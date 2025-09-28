package com.identityaccessdomain.userservice.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * user-service
 * <p>
 * DTO para respostas de erro padronizadas
 *
 * @param timestamp Momento do erro
 * @param status    Código HTTP
 * @param error     Nome do erro (ex.: "Bad Request")
 * @param message   Mensagem amigável
 * @param path      Endpoint que gerou o erro
 * @param fields    Erros de validação específicos por campo (quando aplicável)
 * @author Juliane Maran
 * @since 28/09/2025
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
  LocalDateTime timestamp,
  int status,
  String error,
  String message,
  String path,
  Map<String, String> fields
) {

}
