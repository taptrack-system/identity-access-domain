package com.identityaccessdomain.userservice.api.rest;

import com.identityaccessdomain.userservice.api.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.api.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.application.command.UserCommandService;
import com.identityaccessdomain.userservice.application.query.UserQueryService;
import com.identityaccessdomain.userservice.domain.user.exception.UserNotFoundException;
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
    log.info("Recebida requisição para listar todos os usuários.");
    List<UserResponseDTO> users = userQueryService.findAll();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
    log.info("Recebida requisição para buscar usuário com ID {}", id);
    // O service agora lança UserNotFoundException se não encontrar
    UserResponseDTO user = userQueryService.findById(id)
      .orElseThrow(() -> { // Este orElseThrow é apenas para satisfazer o retorno de UserResponseDTO
        log.error("Erro inesperado: findById retornou Optional.empty mas deveria ter lançado exceção.");
        return new UserNotFoundException("Usuário com ID " + id + " não encontrado.");
      });
    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
    log.info("Recebida requisição para criar usuário: {}", requestDTO.email());
    UserResponseDTO createdUser = userCommandService.create(requestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                    @Valid @RequestBody UserRequestDTO requestDTO) {
    log.info("Recebida requisição para atualizar usuário ID {}", id);
    // O service agora lança UserNotFoundException ou EmailAlreadyExistsException se necessário
    UserResponseDTO updatedUser = userCommandService.update(id, requestDTO)
      .orElseThrow(() -> {
        log.error("Erro inesperado: update retornou Optional.empty mas deveria ter lançado exceção.");
        return new UserNotFoundException("Usuário com ID " + id + " não encontrado para atualização.");
      });
    return ResponseEntity.ok(updatedUser);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserResponseDTO> partialUpdateUser(@PathVariable Long id,
                                                           @RequestBody UserRequestDTO requestDTO) {
    log.info("Recebida requisição para atualização parcial do usuário ID {}", id);
    // O service agora lança UserNotFoundException ou EmailAlreadyExistsException se necessário
    UserResponseDTO partialUpdatedUser = userCommandService.partialUpdate(id, requestDTO)
      .orElseThrow(() -> {
        log.error("Erro inesperado: partialUpdate retornou Optional.empty mas deveria ter lançado exceção.");
        return new UserNotFoundException("Usuário com ID " + id + " não encontrado para atualização parcial.");
      });
    return ResponseEntity.ok(partialUpdatedUser);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.info("Recebida requisição para deletar usuário ID {}", id);
    // O service agora lança UserNotFoundException se não encontrar
    userCommandService.delete(id);
    // Se a deleção falhar e não lançar exceção (o que não deveria acontecer com o código atual),
    // isso seria um caso de lógica de negócio inesperada.
    // Como o delete() no service lança exceção para not found, 'deleted' será sempre true aqui
    // se o usuário for encontrado.
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
