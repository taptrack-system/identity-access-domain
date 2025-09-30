package com.identityaccessdomain.userservice.users.domain.user.service;

import com.identityaccessdomain.userservice.users.domain.user.exception.EmailAlreadyExistsException;
import com.identityaccessdomain.userservice.users.domain.user.exception.UserNotFoundException;
import com.identityaccessdomain.userservice.users.domain.user.model.User;
import com.identityaccessdomain.userservice.users.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 29/09/2025
 * <p>
 * Classe auxiliar para centralizar lógicas de validação de domínio
 * relacionadas a usuários, promovendo o Single Responsibility Principle (SRP)
 * e evitando duplicação de código em serviços.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidationHelper {

  private final UserRepository userRepository;

  /**
   * Verifica se um e-mail já existe para outro usuário.
   * Lança EmailAlreadyExistsException se o e-mail já estiver cadastrado.
   *
   * @param email         O e-mail a ser verificado.
   * @param currentUserId O ID do usuário atual sendo editado (pode ser null para criação).
   */
  public void validateEmailAvailability(String email, Long currentUserId) {
    userRepository.findByEmail(email).ifPresent(existingUser -> {
      if (currentUserId == null || !existingUser.getId().equals(currentUserId)) {
        log.warn("Validação falhou: E-mail {} já cadastrado para outro usuário.", email);
        throw new EmailAlreadyExistsException("O e-mail " + email + " já está cadastrado.");
      }
    });
  }

  /**
   * Busca um usuário pelo ID e lança UserNotFoundException se não for encontrado.
   *
   * @param id O ID do usuário a ser buscado.
   * @return O objeto User encontrado.
   * @throws UserNotFoundException se o usuário não for encontrado.
   */
  public User findUserByIdOrThrow(Long id) {
    return userRepository.findById(id)
      .orElseThrow(() -> {
        log.warn("Validação falhou: Usuário com ID {} não encontrado.", id);
        return new UserNotFoundException("Usuário com ID " + id + " não encontrado.");
      });
  }

}
