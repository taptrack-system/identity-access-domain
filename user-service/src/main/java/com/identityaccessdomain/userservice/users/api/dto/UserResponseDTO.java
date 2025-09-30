package com.identityaccessdomain.userservice.users.api.dto;

import lombok.Builder;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Builder
public record UserResponseDTO(
  Long id,
  String firstName,
  String lastName,
  String email
) {
}
