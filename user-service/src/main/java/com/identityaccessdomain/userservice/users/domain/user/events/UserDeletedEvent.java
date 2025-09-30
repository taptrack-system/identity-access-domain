package com.identityaccessdomain.userservice.users.domain.user.events;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
public record UserDeletedEvent(Object source, Long userId) {
}
