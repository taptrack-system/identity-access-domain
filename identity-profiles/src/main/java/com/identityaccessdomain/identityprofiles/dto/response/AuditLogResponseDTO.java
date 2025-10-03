package com.identityaccessdomain.identityprofiles.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Builder
public record AuditLogResponseDTO(
  Long id,
  String entity,
  Long entityId,
  String action,
  String performedBy,
  LocalDateTime timestamp,
  String details
) {
}
