package com.identityaccessdomain.userservice.domain.user.events;

import lombok.Builder;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Builder
public record UserUpdatedEvent(Object source, Long userId) {
}
