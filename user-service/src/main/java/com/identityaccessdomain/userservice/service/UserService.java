package com.identityaccessdomain.userservice.service;

import com.identityaccessdomain.userservice.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.model.User;

import java.util.List;
import java.util.Optional;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
public interface UserService {

  List<UserResponseDTO> findAllUsers();

  Optional<UserResponseDTO> findUserById(Long id);

  UserResponseDTO createUser(UserRequestDTO requestDTO);

  Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO requestDTO);

  Optional<UserResponseDTO> partialUpdateUser(Long id, UserRequestDTO requestDTO);

  boolean deleteUser(Long id);

}
