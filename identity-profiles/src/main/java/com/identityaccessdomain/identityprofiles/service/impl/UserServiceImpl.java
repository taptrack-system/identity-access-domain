package com.identityaccessdomain.identityprofiles.service.impl;

import com.identityaccessdomain.identityprofiles.domain.entity.Role;
import com.identityaccessdomain.identityprofiles.domain.entity.User;
import com.identityaccessdomain.identityprofiles.domain.enums.RoleType;
import com.identityaccessdomain.identityprofiles.domain.enums.UserStatus;
import com.identityaccessdomain.identityprofiles.dto.request.UserRequestDTO;
import com.identityaccessdomain.identityprofiles.dto.response.UserResponseDTO;
import com.identityaccessdomain.identityprofiles.mapping.UserMapper;
import com.identityaccessdomain.identityprofiles.repositories.RoleRepository;
import com.identityaccessdomain.identityprofiles.repositories.UserRepository;
import com.identityaccessdomain.identityprofiles.service.AuditLogService;
import com.identityaccessdomain.identityprofiles.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AuditLogService auditLogService;

  @Override
  @Transactional
  public UserResponseDTO createUser(UserRequestDTO requestDTO, String performedBy) {
    validarDuplicidadeEmail(requestDTO.email());
    validarDuplicidadeUsername(requestDTO.username());

    User user = userMapper.toEntity(requestDTO);
    user.setStatus(UserStatus.ACTIVE);

    // Mapear roles
    Set<Role> roles = requestDTO.roles().stream()
      .map(roleName -> roleRepository.findByName(RoleType.valueOf(roleName))
        .orElseThrow(() -> new IllegalArgumentException("Role não encontrada: " + roleName)))
      .collect(Collectors.toSet());
    user.setRoles(roles);

    user = userRepository.save(user);

    auditLogService.logEvent("User", user.getId(), "CREATE", performedBy,
      "Usuário criado com roles: " + roles.stream().map(r -> r.getName().name()).toList());

    return userMapper.toResponseDTO(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponseDTO getUserById(Long id) {
    User user = userRepository.findById(id)
      .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com ID: " + id));
    return userMapper.toResponseDTO(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDTO> listUsers() {
    return userRepository.findAll().stream()
      .map(userMapper::toResponseDTO)
      .toList();
  }

  @Override
  @Transactional
  public void deleteUser(Long id, String performedBy) {
    User user = userRepository.findById(id)
      .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com ID: " + id));
    userRepository.delete(user);
    auditLogService.logEvent(
      "User", id,
      "DELETE", performedBy,
      "Usuário " + user.getUsername() + " removido");
  }

  // --- Validations

  private void validarDuplicidadeEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("E-mail já está em uso: " + email);
    }
  }

  private void validarDuplicidadeUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("Username já está em uso: " + username);
    }
  }

}
