package com.identityaccessdomain.userservice.application.query;

import com.identityaccessdomain.userservice.api.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;

/**
 * user-service
 * <p>
 * CQRS: Query (reads) = operações de leitura, como buscar usuários, listar usuários, etc.
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
public interface UserQueryService {

  List<UserResponseDTO> findAll();

  Optional<UserResponseDTO> findById(Long id);

  // adicionais: search by name, pagination, filters...

}
