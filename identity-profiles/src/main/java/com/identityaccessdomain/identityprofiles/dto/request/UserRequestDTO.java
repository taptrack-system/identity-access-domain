package com.identityaccessdomain.identityprofiles.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Builder
public record UserRequestDTO(
  @NotBlank(message = "O username é obrigatório")
  @Size(min = 3, max = 50, message = "O username deve ter entre 3 e 50 caracteres")
  String username,

  @NotBlank(message = "O e-mail é obrigatório")
  @Email(message = "Informe um e-mail válido")
  @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
  String email,

  @NotBlank(message = "A senha é obrigatória")
  @Size(min = 8, max = 255, message = "A senha deve ter entre 8 e 255 caracteres")
  String password,

  @NotBlank(message = "O nome completo é obrigatório")
  @Size(max = 150, message = "O nome completo deve ter no máximo 150 caracteres")
  String fullName,

  @NotEmpty(message = "O usuário deve ter ao menos um perfil")
  Set<String> roles // nomes dos roles (ADMIN, CUSTOMER, etc.)
) {
}
