package com.identityaccessdomain.userservice.controller;

import com.identityaccessdomain.userservice.model.User;
import com.identityaccessdomain.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Slf4j
@RequestMapping
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<User>> listUsers() {
    return ResponseEntity.ok(userService.findAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getById(@PathVariable Long id) {
    return userService.findUserById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

}
