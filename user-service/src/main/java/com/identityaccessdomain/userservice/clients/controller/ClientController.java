package com.identityaccessdomain.userservice.clients.controller;

import com.identityaccessdomain.userservice.clients.domain.Client;
import com.identityaccessdomain.userservice.clients.dto.ClientRequest;
import com.identityaccessdomain.userservice.clients.dto.ClientResponse;
import com.identityaccessdomain.userservice.clients.service.ClientService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 30/09/2025
 */
@Slf4j
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

  private final ClientService clientService;

  @PostMapping
  public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest clientRequest) {
    Client saved = clientService.createClient(clientRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(ClientResponse.fromEntity(saved));
  }

}
