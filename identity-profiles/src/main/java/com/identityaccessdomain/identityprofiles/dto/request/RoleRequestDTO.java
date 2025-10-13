package com.identityaccessdomain.identityprofiles.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Builder
public record RoleRequestDTO(
  @NotBlank(message = "O nome do perfil é obrigatório")
  String name,
  @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
  String description
) {
}
