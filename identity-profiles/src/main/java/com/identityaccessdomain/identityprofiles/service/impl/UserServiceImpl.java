package com.identityaccessdomain.identityprofiles.service.impl;

import com.identityaccessdomain.identityprofiles.domain.entity.Role;
import com.identityaccessdomain.identityprofiles.domain.entity.User;
import com.identityaccessdomain.identityprofiles.domain.enums.RoleType;
import com.identityaccessdomain.identityprofiles.domain.enums.UserStatus;
import com.identityaccessdomain.identityprofiles.dto.request.UserRequestDTO;
import com.identityaccessdomain.identityprofiles.dto.response.UserResponseDTO;
import com.identityaccessdomain.identityprofiles.exception.ConflictException;
import com.identityaccessdomain.identityprofiles.exception.ResourceNotFoundException;
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

  /**
   * Cadastrar Usuário
   *
   * @param requestDTO  Campos necessários para cadastrar o usuário
   * @param performedBy Usuário responsável
   * @return Retorna as informações do usuário cadastrado
   */
  @Override
  @Transactional
  public UserResponseDTO createUser(UserRequestDTO requestDTO, String performedBy) {
    log.info("Iniciando criação de usuário: username={}, email={}", requestDTO.username(), requestDTO.email());

    validateEmailDuplication(requestDTO.email());
    validateUsernameDuplication(requestDTO.username());

    User user = userMapper.toEntity(requestDTO);
    user.setStatus(UserStatus.ACTIVE);

    Set<Role> roles = mapRolesFromRequest(requestDTO.roles());
    user.setRoles(roles);

    user = userRepository.save(user);
    log.info("Usuário criado com sucesso: id={}, username={}", user.getId(), user.getUsername());

    auditLogService.logEvent(
      "User",
      user.getId(),
      "CREATE",
      performedBy,
      "Usuário criado com roles: " + roles.stream().map(r -> r.getName().name()).toList()
    );

    return userMapper.toResponseDTO(user);
  }

  /**
   * Obter Usuário por ID
   *
   * @param id Identificador Único do Usuário
   * @return Retorna UserResponseDTO
   */
  @Override
  @Transactional(readOnly = true)
  public UserResponseDTO getUserById(Long id) {
    log.info("Buscando usuário pelo ID: {}", id);

    User user = findUserByIdOrThrow(id);

    log.info("Usuário encontrado: id={}, username={}", user.getId(), user.getUsername());
    return userMapper.toResponseDTO(user);
  }

  /**
   * Listar Usuários
   *
   * @return Retorna uma Lista de Usuários, se não existir nenhum usuário cadastrado na lista retornada é vazia
   */
  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDTO> listUsers() {
    log.info("Listando todos os usuários");

    List<UserResponseDTO> users = userRepository.findAll()
      .stream()
      .map(userMapper::toResponseDTO)
      .toList();

    log.info("Total de usuários encontrados: {}", users.size());
    return users;
  }

  /**
   * Excluir Usuário por ID
   *
   * @param id          Identificação única do Usuário
   * @param performedBy Usuário responsável
   */
  @Override
  @Transactional
  public void deleteUser(Long id, String performedBy) {
    log.info("Tentando remover usuário pelo ID: {}", id);
    User user = findUserByIdOrThrow(id);
    log.info("Usuário removido com sucesso: id={}, username={}", user.getId(), user.getUsername());

    auditLogService.logEvent(
      "User",
      id,
      "DELETE",
      performedBy,
      "Usuário " + user.getUsername() + " removido"
    );
  }

  // --- Métodos Auxiliares

  /**
   * Validar Duplicidade de E-mail
   *
   * @param email E-mail informado
   * @throws ConflictException Lança exceção para o e-mail não encontrado
   */
  private void validateEmailDuplication(String email) {
    log.debug("Validando duplicidade de e-mail: {}", email);
    if (userRepository.existsByEmail(email)) {
      log.warn("E-mail já está em uso: {}", email);
      throw new ConflictException("E-mail já está em uso: " + email);
    }
  }

  /**
   * Validar Duplicidade de Username
   *
   * @param username Nome de usuário
   * @throws ConflictException Lança exceção para username não encontrada.
   */
  private void validateUsernameDuplication(String username) {
    log.debug("Validando duplicidade de username: {}", username);
    if (userRepository.existsByUsername(username)) {
      log.warn("Username já está em uso: {}", username);
      throw new ConflictException("Username já está em uso: " + username);
    }
  }

  /**
   * Centraliza busca por ID e lança exceção
   *
   * @param id Identificador Único do Usuário
   * @return Retorna o ID
   * @throws ResourceNotFoundException Lança exceção para usuário não encontrado
   */
  private User findUserByIdOrThrow(Long id) {
    return userRepository.findById(id)
      .orElseThrow(() -> {
        log.error("Usuário não encontrado com ID: {}", id);
        return new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
      });
  }

  /**
   * Centraliza mapeamento de roles
   *
   * @param roleNames Nome do papel (role)
   * @return Retorna uma lista de pápeis (roles)
   * @throws ResourceNotFoundException Lançar exceção para papel (role) não encontrada
   */
  private Set<Role> mapRolesFromRequest(Set<String> roleNames) {
    log.debug("Mapeando roles do usuário: {}", roleNames);
    return roleNames.stream()
      .map(roleName -> roleRepository.findByName(RoleType.valueOf(roleName))
        .orElseThrow(() -> {
          log.error("Role não encontrada: {}", roleName);
          return new ResourceNotFoundException("Role não encontrada: " + roleName);
        })
      )
      .collect(Collectors.toSet());
  }

}
