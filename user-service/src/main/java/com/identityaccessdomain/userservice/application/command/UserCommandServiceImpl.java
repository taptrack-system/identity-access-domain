package com.identityaccessdomain.userservice.application.command;

import com.identityaccessdomain.userservice.api.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.api.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.application.mapping.UserMapper;
import com.identityaccessdomain.userservice.domain.user.events.UserCreatedEvent;
import com.identityaccessdomain.userservice.domain.user.events.UserDeletedEvent;
import com.identityaccessdomain.userservice.domain.user.events.UserUpdatedEvent;
import com.identityaccessdomain.userservice.domain.user.exception.EmailAlreadyExistsException;
import com.identityaccessdomain.userservice.domain.user.exception.UserNotFoundException;
import com.identityaccessdomain.userservice.domain.user.model.User;
import com.identityaccessdomain.userservice.domain.user.repository.UserRepository;
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
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public UserResponseDTO create(UserRequestDTO dto) {
    log.info("Iniciando criação de usuário com e-mail: {}", dto.email());

    if (userRepository.existsByEmail(dto.email())) {
      log.warn("Falha na criação: e-mail {} já cadastrado", dto.email());
      throw new EmailAlreadyExistsException("O e-mail " + dto.email() + " já está cadastrado.");
    }

    User entity = userMapper.requestDtoToEntity(dto);
    User saved = userRepository.save(entity);

    log.info("Usuário criado com sucesso. ID: {}", saved.getId());
    eventPublisher.publishEvent(new UserCreatedEvent(this, saved.getId()));

    return userMapper.entityToResponseDto(saved);
  }

  @Override
  @Transactional
  public Optional<UserResponseDTO> update(Long id, UserRequestDTO dto) {
    log.info("Iniciando atualização completa do usuário ID {}", id);

    User existingUser = userRepository.findById(id)
      .orElseThrow(() -> {
        log.warn("Falha na atualização: Usuário com ID {} não encontrado.", id);
        return new UserNotFoundException("Usuário com ID " + id + " não encontrado para atualização.");
      });

    if (!existingUser.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
      log.warn("Falha na atualização: O novo e-mail {} já está cadastrado para outro usuário.", dto.email());
      throw new EmailAlreadyExistsException("O e-mail " + dto.email() + " já está cadastrado.");
    }

    userMapper.updateEntityFromDto(dto, existingUser);

    User updatedUser = userRepository.save(existingUser);

    log.info("Usuário ID {} atualizado com sucesso.", updatedUser.getId());
    eventPublisher.publishEvent(new UserUpdatedEvent(this, updatedUser.getId()));

    return Optional.of(userMapper.entityToResponseDto(updatedUser));
  }

  @Override
  @Transactional
  public Optional<UserResponseDTO> partialUpdate(Long id, UserRequestDTO dto) {
    log.info("Iniciando atualização parcial do usuário ID {}", id);

    User existing = userRepository.findById(id)
      .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + id + " não encontrado."));

    userMapper.updateEntityFromDto(dto, existing);

    if (dto.email() != null && !existing.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
      log.warn("Falha na atualização parcial: e-mail {} já cadastrado", dto.email());
      throw new EmailAlreadyExistsException("O e-mail " + dto.email() + " já está cadastrado.");
    }

    User saved = userRepository.save(existing);

    log.info("Atualização parcial concluída para usuário ID {}", saved.getId());
    eventPublisher.publishEvent(new UserUpdatedEvent(this, saved.getId()));

    return Optional.of(userMapper.entityToResponseDto(saved));
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    log.info("Tentativa de exclusão do usuário ID {}", id);

    User existing = userRepository.findById(id)
      .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + id + " não encontrado."));
    userRepository.delete(existing);
    log.info("Usuário ID {} deletado com sucesso", id);
    eventPublisher.publishEvent(new UserDeletedEvent(this, id));
    return true;
  }

}
