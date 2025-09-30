package com.identityaccessdomain.userservice.users.application.query;

import com.identityaccessdomain.userservice.users.api.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.users.application.mapping.UserMapper;
import com.identityaccessdomain.userservice.users.domain.user.exception.UserNotFoundException;
import com.identityaccessdomain.userservice.users.domain.user.service.UserValidationHelper;
import com.identityaccessdomain.userservice.users.infra.search.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

  private final UserMapper userMapper;
  private final UserValidationHelper validationHelper;
  private final UserSearchRepository searchRepository;

  @Override
  public List<UserResponseDTO> findAll() {
    log.debug("Buscando todos os usuários no Elasticsearch.");
    return StreamSupport.stream(searchRepository.findAll().spliterator(), false)
      .map(userMapper::documentToResponseDto)
      .collect(Collectors.toList());
  }

  @Override
  public UserResponseDTO findById(Long id) {
    log.debug("Buscando usuário ID {} no Elasticsearch.", id);
    return searchRepository.findById(id)
      .map(userMapper::documentToResponseDto)
      .orElseThrow(() -> {
        log.warn("Usuário com ID {} não encontrado no Elasticsearch (Query Service).", id);
        return new UserNotFoundException("Usuário com ID " + id + " não encontrado.");
      });
  }

}
