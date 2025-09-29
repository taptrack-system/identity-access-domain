package com.identityaccessdomain.userservice.application.query;

import com.identityaccessdomain.userservice.api.dto.UserResponseDTO;

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
