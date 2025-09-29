package com.identityaccessdomain.userservice.application.command;

import com.identityaccessdomain.userservice.api.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.api.dto.UserResponseDTO;

import java.util.Optional;

/**
 * user-service
 * <p>
 * CQRS: Command (writes) = operações que modificam o estado (create, update, delete)
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
public interface UserCommandService {

  UserResponseDTO create(UserRequestDTO dto);

  Optional<UserResponseDTO> update(Long id, UserRequestDTO dto);

  Optional<UserResponseDTO> partialUpdate(Long id, UserRequestDTO dto);

  boolean delete(Long id);

}
