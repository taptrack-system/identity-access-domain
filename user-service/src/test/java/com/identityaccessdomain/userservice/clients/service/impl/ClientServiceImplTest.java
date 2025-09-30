package com.identityaccessdomain.userservice.clients.service.impl;

import com.identityaccessdomain.userservice.clients.domain.Client;
import com.identityaccessdomain.userservice.clients.dto.ClientRequest;
import com.identityaccessdomain.userservice.clients.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

  ClientRepository clientRepository;

  ClientServiceImpl clientService;

  Client client;

  ClientRequest clientRequest;

  @BeforeEach
  void setUp() {
    clientRepository = mock(ClientRepository.class);
    clientService = new ClientServiceImpl(clientRepository);
    // Given
    clientRequest = ClientRequest.builder()
      .name("João da Silva")
      .phone("+5511912345678")
      .email("joao.silva@example.com")
      .build();

    client = Client.builder()
      .id(1L)
      .name(clientRequest.name())
      .phone(clientRequest.phone())
      .email(clientRequest.email())
      .build();
  }

  @Test
  @DisplayName("Deve salvar cliente com dados válidos")
  void shouldSaveClientWithValidData() {
    when(clientRepository.save(any(Client.class))).thenReturn(client);

    // when
    Client saved = clientService.createClient(clientRequest);

    // then
    assertThat(saved.getId()).isEqualTo(1L);
    assertThat(saved.getName()).isEqualTo("João da Silva");
    assertThat(saved.getPhone()).isEqualTo("+5511912345678");
    assertThat(saved.getEmail()).isEqualTo("joao.silva@example.com");

    verify(clientRepository, times(1)).save(any(Client.class));

  }
}