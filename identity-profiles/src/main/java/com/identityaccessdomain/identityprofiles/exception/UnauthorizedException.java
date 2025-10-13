package com.identityaccessdomain.identityprofiles.exception;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 03/10/2025
 */
public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException(String message) {
    super(message);
  }
}
