package com.identityaccessdomain.userservice.api.rest;

import com.identityaccessdomain.userservice.api.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.api.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.application.command.UserCommandService;
import com.identityaccessdomain.userservice.application.query.UserQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

  private final UserQueryService userQueryService;
  private final UserCommandService userCommandService;

  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> listUsers() {
    log.debug("GET /users - Listar todos os usuários.");
    List<UserResponseDTO> users = userQueryService.findAll();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
    log.debug("GET /users/{} - Buscar usuário por ID.", id);
    UserResponseDTO user = userQueryService.findById(id); // Chama o serviço
    return ResponseEntity.ok(user); // Retorna o DTO diretamente, sabendo que user nunca será nulo aqui
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
    log.debug("POST /users - Criar novo usuário com e-mail: {}", requestDTO.email());
    UserResponseDTO createdUser = userCommandService.create(requestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                    @Valid @RequestBody UserRequestDTO requestDTO) {
    log.debug("PUT /users/{} - Atualizar usuário por ID.", id);
    UserResponseDTO updatedUser = userCommandService.update(id, requestDTO); // O serviço já lança a exceção
    return ResponseEntity.ok(updatedUser);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserResponseDTO> partialUpdateUser(@PathVariable Long id,
                                                           @RequestBody UserRequestDTO requestDTO) {
    log.debug("PATCH /users/{} - Atualização parcial do usuário por ID.", id);
    UserResponseDTO partialUpdatedUser = userCommandService.partialUpdate(id, requestDTO); // O serviço já lança a exceção
    return ResponseEntity.ok(partialUpdatedUser);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.debug("DELETE /users/{} - Deletar usuário por ID.", id);
    userCommandService.delete(id); // O serviço já lança a exceção
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
