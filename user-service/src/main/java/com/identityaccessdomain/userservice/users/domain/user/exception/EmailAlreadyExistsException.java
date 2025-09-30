package com.identityaccessdomain.userservice.users.domain.user.exception;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
public class EmailAlreadyExistsException extends RuntimeException {

  public EmailAlreadyExistsException(String message) {
    super(message);
  }

}
