package com.identityaccessdomain.identityprofiles.controllers;

import com.identityaccessdomain.identityprofiles.dto.request.UserRequestDTO;
import com.identityaccessdomain.identityprofiles.dto.response.UserResponseDTO;
import com.identityaccessdomain.identityprofiles.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO,
                                                    @RequestHeader("X-Performed-By") String performedBy) {
    UserResponseDTO responseDTO = userService.createUser(requestDTO, performedBy);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> listUsers() {
    return ResponseEntity.ok(userService.listUsers());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id,
                                         @RequestHeader("X-Performed-By") String performedBy) {
    userService.deleteUser(id, performedBy);
    return ResponseEntity.noContent().build();
  }

}
