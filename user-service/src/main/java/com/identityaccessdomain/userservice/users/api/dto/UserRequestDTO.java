package com.identityaccessdomain.userservice.users.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Builder
public record UserRequestDTO(

  @NotBlank(message = "O primeiro nome é obrigatório.")
  @Size(min = 3, max = 50, message = "O primeiro nome deve ter entre {min} e {max} caracteres.")
  String firstName,

  @NotBlank(message = "O sobrenome é obrigatório.")
  @Size(min = 3, max = 50, message = "O sobrenome deve ter entre {min} e {max} caracteres.")
  String lastName,

  @NotBlank(message = "O e-mail é obrigatório.")
  @Email(message = "O e-mail deve ser válido.")
  @Size(max = 150, message = "O e-mail deve ter no máximo {max} caracteres.")
  String email
) {
}
