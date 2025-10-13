package com.identityaccessdomain.identityprofiles.repositories;

import com.identityaccessdomain.identityprofiles.domain.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
