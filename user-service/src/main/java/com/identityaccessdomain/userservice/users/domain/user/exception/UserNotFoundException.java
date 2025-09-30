package com.identityaccessdomain.userservice.users.domain.user.exception;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }

}
