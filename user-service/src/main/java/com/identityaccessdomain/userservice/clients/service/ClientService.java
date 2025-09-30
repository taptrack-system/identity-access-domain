package com.identityaccessdomain.userservice.clients.service;

import com.identityaccessdomain.userservice.clients.domain.Client;
import com.identityaccessdomain.userservice.clients.dto.ClientRequest;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 30/09/2025
 */
public interface ClientService {

  Client createClient(ClientRequest clientRequest);

}
