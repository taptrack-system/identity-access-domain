package com.identityaccessdomain.userservice.clients.repository;

import com.identityaccessdomain.userservice.clients.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 30/09/2025
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
  Optional<Client> findByEmail(String email);
}
