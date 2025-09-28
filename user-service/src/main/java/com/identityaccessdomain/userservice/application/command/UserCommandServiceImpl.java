package com.identityaccessdomain.userservice.application.command;

import com.identityaccessdomain.userservice.domain.user.events.UserCreatedEvent;
import com.identityaccessdomain.userservice.domain.user.events.UserDeletedEvent;
import com.identityaccessdomain.userservice.domain.user.events.UserUpdatedEvent;
import com.identityaccessdomain.userservice.domain.user.model.User;
import com.identityaccessdomain.userservice.domain.user.repository.UserRepository;
import com.identityaccessdomain.userservice.api.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.api.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.application.mapping.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;

  // para publicar eventos de domínio
  // O listener com @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  // para garantir que a indexação só ocorra após o commit da transação
  // evitando inconsistências entre o banco de dados e o Elasticsearch
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public UserResponseDTO create(UserRequestDTO dto) {
    User entity = userMapper.requestDtoToEntity(dto);
    User saved = userRepository.save(entity);

    // publica evento para indexação em ES *após commit*
    eventPublisher.publishEvent(new UserCreatedEvent(this, saved.getId()));

    return userMapper.entityToResponseDto(saved);
  }

  @Override
  @Transactional
  public Optional<UserResponseDTO> update(Long id, UserRequestDTO dto) {
    return userRepository.findById(id).map(existing -> {
      User updated = userMapper.requestDtoToEntity(dto);
      updated.setId(existing.getId());
      User saved = userRepository.save(updated);
      eventPublisher.publishEvent(new UserUpdatedEvent(this, saved.getId()));
      return userMapper.entityToResponseDto(saved);
    });
  }

  @Override
  @Transactional
  public Optional<UserResponseDTO> partialUpdate(Long id, UserRequestDTO dto) {
    return userRepository.findById(id).map(existing -> {
      if (dto.firstName() != null) existing.setFirstName(dto.firstName());
      if (dto.lastName() != null) existing.setLastName(dto.lastName());
      if (dto.email() != null) existing.setEmail(dto.email());
      User saved = userRepository.save(existing);
      eventPublisher.publishEvent(new UserUpdatedEvent(this, saved.getId()));
      return userMapper.entityToResponseDto(saved);
    });
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    return userRepository.findById(id).map(u -> {
      userRepository.delete(u);
      eventPublisher.publishEvent(new UserDeletedEvent(this, id));
      return true;
    }).orElse(false);
  }

}
