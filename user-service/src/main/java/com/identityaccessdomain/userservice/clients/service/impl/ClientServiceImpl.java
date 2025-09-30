package com.identityaccessdomain.userservice.clients.service.impl;

import com.identityaccessdomain.userservice.clients.domain.Client;
import com.identityaccessdomain.userservice.clients.dto.ClientRequest;
import com.identityaccessdomain.userservice.clients.repository.ClientRepository;
import com.identityaccessdomain.userservice.clients.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 30/09/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final ClientRepository clientRepository;

  @Override
  @Transactional
  public Client createClient(ClientRequest clientRequest) {

    // TODO: Verificar se já existe cliente com mesmo e-mail
    var client = Client.builder()
      .name(clientRequest.name())
      .phone(clientRequest.phone())
      .email(clientRequest.email())
      .build();

    return clientRepository.save(client);
  }

}
