package com.identityaccessdomain.identityprofiles.dto.response;

import com.identityaccessdomain.identityprofiles.domain.enums.UserStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Builder
public record UserResponseDTO(
  Long id,
  String username,
  String email,
  String fullName,
  UserStatus status,
  Set<String> roles,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {
}
