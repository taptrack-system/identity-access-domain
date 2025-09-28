package com.identityaccessdomain.userservice.service.impl;

import com.identityaccessdomain.userservice.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.mapping.UserMapper;
import com.identityaccessdomain.userservice.model.User;
import com.identityaccessdomain.userservice.repository.UserRepository;
import com.identityaccessdomain.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDTO> findAllUsers() {
    return userRepository.findAll()
      .stream()
      .map(userMapper::entityToResponseDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<UserResponseDTO> findUserById(Long id) {
    return userRepository.findById(id)
      .map(userMapper::entityToResponseDto);
  }

  @Override
  @Transactional
  public UserResponseDTO createUser(UserRequestDTO requestDTO) {
    User user = userMapper.requestDtoToEntity(requestDTO);
    log.info("Criando novo usuário: {}", user);
    User savedUser = userRepository.save(user);
    return userMapper.entityToResponseDto(savedUser);
  }

  @Override
  @Transactional
  public Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO requestDTO) {
    return userRepository.findById(id).map(existingUser -> {
      User updatedUser = userMapper.requestDtoToEntity(requestDTO);
      updatedUser.setId(existingUser.getId());
      log.info("Atualizando usuário ID {}: {}", id, updatedUser);
      return userMapper.entityToResponseDto(userRepository.save(updatedUser));
    });
  }

  @Override
  @Transactional
  public Optional<UserResponseDTO> partialUpdateUser(Long id, UserRequestDTO requestDTO) {
    return userRepository.findById(id).map(existingUser -> {
      if (requestDTO.firstName() != null) existingUser.setFirstName(requestDTO.firstName());
      if (requestDTO.lastName() != null) existingUser.setLastName(requestDTO.lastName());
      if (requestDTO.email() != null) existingUser.setEmail(requestDTO.email());
      log.info("Atualização parcial do usuário ID {}: {}", id, existingUser);
      return userMapper.entityToResponseDto(userRepository.save(existingUser));
    });
  }

  @Override
  @Transactional
  public boolean deleteUser(Long id) {
    return userRepository.findById(id).map(user -> {
      userRepository.delete(user);
      log.info("Usuário ID {} deletado", id);
      return true;
    }).orElse(false);
  }

}
