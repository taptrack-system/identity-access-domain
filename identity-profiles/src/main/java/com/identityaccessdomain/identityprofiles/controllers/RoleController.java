package com.identityaccessdomain.identityprofiles.controllers;

import com.identityaccessdomain.identityprofiles.dto.request.RoleRequestDTO;
import com.identityaccessdomain.identityprofiles.dto.response.RoleResponseDTO;
import com.identityaccessdomain.identityprofiles.service.RoleService;
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
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
  // Todo: Incluir Logs (em Português)
  private final RoleService roleService;

  @PostMapping
  public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody RoleRequestDTO request,
                                                    @RequestHeader("X-Performed-By") String performedBy) {
    RoleResponseDTO role = roleService.createRole(request, performedBy);
    return ResponseEntity.status(HttpStatus.CREATED).body(role);
  }

  @GetMapping("/{name}")
  public ResponseEntity<RoleResponseDTO> getRole(@PathVariable String name) {
    return ResponseEntity.ok(roleService.getRoleByName(name));
  }

  @GetMapping
  public ResponseEntity<List<RoleResponseDTO>> listRoles() {
    return ResponseEntity.ok(roleService.listRoles());
  }

}
