package com.identityaccessdomain.userservice.controller;

import com.identityaccessdomain.userservice.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.mapping.UserMapper;
import com.identityaccessdomain.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Slf4j
@RequestMapping
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserMapper userMapper;
  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> listUsers() {
    log.info("Listando todos os usuários");
    List<UserResponseDTO> users = userService.findAllUsers()
      .stream()
      .map(userMapper::entityToResponseDto)
      .collect(Collectors.toList());
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
    log.info("Buscando usuário com ID {}", id);
    return userService.findUserById(id)
      .map(userMapper::entityToResponseDto)
      .map(ResponseEntity::ok)
      .orElseGet(() -> {
        log.warn("Usuário com ID {} não encontrado", id);
        return ResponseEntity.notFound().build();
      });
  }

}
