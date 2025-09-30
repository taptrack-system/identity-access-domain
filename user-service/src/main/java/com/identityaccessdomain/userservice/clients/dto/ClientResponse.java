package com.identityaccessdomain.userservice.clients.dto;

import com.identityaccessdomain.userservice.clients.domain.Client;
import lombok.Builder;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 30/09/2025
 */
@Builder
public record ClientResponse(
  Long id,
  String name,
  String phone,
  String email,
  String message
) {
  public static ClientResponse fromEntity(Client client) {
    return new ClientResponse(
      client.getId(),
      client.getName(),
      client.getPhone(),
      client.getEmail(),
      "Cliente cadastrado com sucesso"
    );
  }
}
