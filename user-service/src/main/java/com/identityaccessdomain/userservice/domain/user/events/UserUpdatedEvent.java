package com.identityaccessdomain.userservice.domain.user.events;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
public record UserUpdatedEvent(Object source, Long userId) {
}
