package com.identityaccessdomain.userservice.controller;

import com.identityaccessdomain.userservice.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  // Listar todos os usuários
  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> listUsers() {
    log.info("Listando todos os usuários");
    return ResponseEntity.ok(userService.findAllUsers());
  }

  // Buscar usuário por ID
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
    log.info("Buscando usuário com ID {}", id);
    return userService.findUserById(id)
      .map(ResponseEntity::ok)
      .orElseGet(() -> {
        log.warn("Usuário com ID {} não encontrado", id);
        return ResponseEntity.notFound().build();
      });
  }

  // Criar novo usuário
  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
    log.info("Criando usuário");
    UserResponseDTO createdUser = userService.createUser(requestDTO);
    return ResponseEntity.status(201).body(createdUser);
  }

  // Atualizar usuário existente
  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                    @Valid @RequestBody UserRequestDTO requestDTO) {
    log.info("Atualizando usuário ID {}", id);
    return userService.updateUser(id, requestDTO)
      .map(ResponseEntity::ok)
      .orElseGet(() -> {
        log.warn("Usuário com ID {} não encontrado para atualização", id);
        return ResponseEntity.notFound().build();
      });
  }

  // Atualização parcial do usuário
  @PatchMapping("/{id}")
  public ResponseEntity<UserResponseDTO> partialUpdateUser(@PathVariable Long id,
                                                           @RequestBody UserRequestDTO requestDTO) {
    log.info("Atualização parcial do usuário ID {}", id);
    return userService.partialUpdateUser(id, requestDTO)
      .map(ResponseEntity::ok)
      .orElseGet(() -> {
        log.warn("Usuário com ID {} não encontrado para atualização parcial", id);
        return ResponseEntity.notFound().build();
      });
  }

  // Deletar usuário
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.info("Deletando usuário ID {}", id);
    boolean deleted = userService.deleteUser(id);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

}
