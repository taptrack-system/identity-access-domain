package com.identityaccessdomain.userservice.application.query;

import com.identityaccessdomain.userservice.api.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.application.mapping.UserMapper;
import com.identityaccessdomain.userservice.infra.search.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
  private final UserSearchRepository searchRepository;

  @Override
  public List<UserResponseDTO> findAll() {
    log.info("Buscando todos os usuários no Elasticsearch");
    return StreamSupport.stream(searchRepository.findAll().spliterator(), false)
      .map(userMapper::documentToResponseDto)
      .collect(Collectors.toList());
  }

  @Override
  public Optional<UserResponseDTO> findById(Long id) {
    log.info("Buscando usuário ID {} no Elasticsearch", id);
    return searchRepository.findById(id).map(userMapper::documentToResponseDto);
  }

}
