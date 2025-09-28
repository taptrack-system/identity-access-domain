package com.identityaccessdomain.userservice.domain.user.repository;

import com.identityaccessdomain.userservice.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
