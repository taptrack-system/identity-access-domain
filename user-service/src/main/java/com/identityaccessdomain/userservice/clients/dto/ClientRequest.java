package com.identityaccessdomain.userservice.clients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 30/09/2025
 */
@Builder
public record ClientRequest(
  @NotBlank(message = "Nome é obrigatório")
  String name,
  @NotBlank(message = "Telefone é obrigatório")
  @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Telefone inválido")
  String phone,
  @NotBlank(message = "E-mail é obrigatório")
  @Email(message = "E-mail inválido")
  String email) {

}
