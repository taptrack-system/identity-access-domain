package com.identityaccessdomain.identityprofiles.repositories;

import com.identityaccessdomain.identityprofiles.domain.entity.Role;
import com.identityaccessdomain.identityprofiles.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(RoleType name);

}
