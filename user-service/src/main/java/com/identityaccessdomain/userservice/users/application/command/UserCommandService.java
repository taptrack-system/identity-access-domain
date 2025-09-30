package com.identityaccessdomain.userservice.users.application.command;

import com.identityaccessdomain.userservice.users.api.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.users.api.dto.UserResponseDTO;

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

  UserResponseDTO update(Long id, UserRequestDTO dto);

  UserResponseDTO partialUpdate(Long id, UserRequestDTO dto);

  void delete(Long id);

}
