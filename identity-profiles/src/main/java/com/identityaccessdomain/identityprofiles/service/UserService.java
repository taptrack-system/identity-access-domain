package com.identityaccessdomain.identityprofiles.service;

import com.identityaccessdomain.identityprofiles.dto.request.UserRequestDTO;
import com.identityaccessdomain.identityprofiles.dto.response.UserResponseDTO;

import java.util.List;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
public interface UserService {

  UserResponseDTO createUser(UserRequestDTO requestDTO, String performedBy);

  UserResponseDTO getUserById(Long id);

  List<UserResponseDTO> listUsers();

  void deleteUser(Long id, String performedByString);

}
