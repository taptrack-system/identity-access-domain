package com.identityaccessdomain.identityprofiles.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 03/10/2025
 */
@Builder
public record ErrorResponse(
  int status,
  String error,
  String message,
  String path,
  LocalDateTime timestamp
) {
}
