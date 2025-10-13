package com.identityaccessdomain.identityprofiles.exception;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 03/10/2025
 */
public class ForbiddenException extends RuntimeException {
  public ForbiddenException(String message) {
    super(message);
  }
}
