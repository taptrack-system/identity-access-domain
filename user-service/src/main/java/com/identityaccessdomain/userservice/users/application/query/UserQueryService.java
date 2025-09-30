package com.identityaccessdomain.userservice.users.application.query;

import com.identityaccessdomain.userservice.users.api.dto.UserResponseDTO;

import java.util.List;

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

  UserResponseDTO findById(Long id);

}
