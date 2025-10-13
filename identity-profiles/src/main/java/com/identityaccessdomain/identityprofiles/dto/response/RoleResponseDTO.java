package com.identityaccessdomain.identityprofiles.dto.response;

import lombok.Builder;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Builder
public record RoleResponseDTO(
  Long id,
  String name,
  String description
) {
}
