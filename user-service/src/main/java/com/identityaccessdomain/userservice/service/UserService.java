package com.identityaccessdomain.userservice.service;

import com.identityaccessdomain.userservice.model.User;

import java.util.List;
import java.util.Optional;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
public interface UserService {

  List<User> findAllUsers();

  Optional<User> findUserById(Long id);

}
