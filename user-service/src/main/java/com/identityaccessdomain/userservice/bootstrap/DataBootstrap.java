package com.identityaccessdomain.userservice.bootstrap;

import com.identityaccessdomain.userservice.model.User;
import com.identityaccessdomain.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataBootstrap implements CommandLineRunner {

  private final UserRepository userRepository;

  @Override
  public void run(String... args) throws Exception {
    if (userRepository.count() == 0) {
      userRepository.save(User.builder().firstName("Alice").lastName("Silva").email("alice@example.com").build());
      userRepository.save(User.builder().firstName("Bruno").lastName("Costa").email("bruno@example.com").build());
      userRepository.save(User.builder().firstName("Carla").lastName("Santos").email("carla@example.com").build());
    }
  }

}
