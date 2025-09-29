package com.identityaccessdomain.userservice.application.command;

import com.identityaccessdomain.userservice.api.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.api.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.application.mapping.UserMapper;
import com.identityaccessdomain.userservice.domain.user.events.UserCreatedEvent;
import com.identityaccessdomain.userservice.domain.user.events.UserDeletedEvent;
import com.identityaccessdomain.userservice.domain.user.events.UserUpdatedEvent;
import com.identityaccessdomain.userservice.domain.user.model.User;
import com.identityaccessdomain.userservice.domain.user.repository.UserRepository;
import com.identityaccessdomain.userservice.domain.user.service.UserValidationHelper;
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
  private final UserValidationHelper validationHelper;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public UserResponseDTO create(UserRequestDTO dto) {
    log.debug("Criando usuário com e-mail: {}", dto.email());

    validationHelper.validateEmailAvailability(dto.email(), null); // Valida e-mail para novo usuário

    User entity = userMapper.requestDtoToEntity(dto);
    User saved = userRepository.save(entity);

    log.info("Usuário criado com sucesso. ID: {}", saved.getId());
    eventPublisher.publishEvent(new UserCreatedEvent(this, saved.getId()));

    return userMapper.entityToResponseDto(saved);
  }

  @Override
  @Transactional
  public Optional<UserResponseDTO> update(Long id, UserRequestDTO dto) {
    log.debug("Atualizando usuário ID {}", id);

    User existingUser = validationHelper.findUserByIdOrThrow(id); // Busca ou lança exceção

    validationHelper.validateEmailAvailability(dto.email(), id); // Valida e-mail para atualização

    userMapper.updateEntityFromDto(dto, existingUser); // Atualiza os campos do DTO na entidade
    User updatedUser = userRepository.save(existingUser);

    log.info("Usuário ID {} atualizado com sucesso.", updatedUser.getId());
    eventPublisher.publishEvent(new UserUpdatedEvent(this, updatedUser.getId()));

    return Optional.ofNullable(userMapper.entityToResponseDto(updatedUser));
  }

  @Override
  @Transactional
  public Optional<UserResponseDTO> partialUpdate(Long id, UserRequestDTO dto) {
    log.debug("Atualização parcial do usuário ID {}", id);

    User existing = validationHelper.findUserByIdOrThrow(id); // Busca ou lança exceção

    // O MapStruct pode ser configurado para ignorar nulos, mas uma verificação explícita
    // antes da validação do e-mail é mais segura para evitar checar e-mail nulo no helper
    if (dto.email() != null) {
      validationHelper.validateEmailAvailability(dto.email(), id);
    }

    userMapper.updateEntityFromDto(dto, existing); // Atualiza os campos não nulos do DTO na entidade
    User saved = userRepository.save(existing);

    log.info("Atualização parcial concluída para usuário ID {}", saved.getId());
    eventPublisher.publishEvent(new UserUpdatedEvent(this, saved.getId()));

    return Optional.ofNullable(userMapper.entityToResponseDto(saved));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.debug("Deletando usuário ID {}", id);

    User existing = validationHelper.findUserByIdOrThrow(id); // Busca ou lança exceção
    userRepository.delete(existing);

    log.info("Usuário ID {} deletado com sucesso", id);
    eventPublisher.publishEvent(new UserDeletedEvent(this, id));
  }

}
