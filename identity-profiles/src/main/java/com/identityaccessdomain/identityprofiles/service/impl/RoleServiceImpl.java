package com.identityaccessdomain.identityprofiles.service.impl;

import com.identityaccessdomain.identityprofiles.domain.entity.Role;
import com.identityaccessdomain.identityprofiles.domain.enums.RoleType;
import com.identityaccessdomain.identityprofiles.dto.request.RoleRequestDTO;
import com.identityaccessdomain.identityprofiles.dto.response.RoleResponseDTO;
import com.identityaccessdomain.identityprofiles.mapping.RoleMapper;
import com.identityaccessdomain.identityprofiles.repositories.RoleRepository;
import com.identityaccessdomain.identityprofiles.service.AuditLogService;
import com.identityaccessdomain.identityprofiles.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleMapper roleMapper;
  private final RoleRepository roleRepository;
  private final AuditLogService auditLogService;

  @Override
  public RoleResponseDTO createRole(RoleRequestDTO requestDTO, String performedBy) {
    // Converte nome -> RoleType de forma segura (RoleMapper lança IllegalArgumentException se inválido)
    Role role = roleMapper.toEntity(requestDTO);

    roleRepository.findByName(role.getName()).ifPresent(r -> {
      log.info("Role {} já existe na base de dados.", r.getName());
      throw new IllegalArgumentException("Role já existe: " + r.getName());
    });

    role = roleRepository.save(role);

    auditLogService.logEvent("Role", role.getId(), "CREATE", performedBy,
      "Role criada: " + role.getName());

    return roleMapper.toResponseDTO(role);
  }

  @Override
  @Transactional(readOnly = true)
  public RoleResponseDTO getRoleByName(String name) {
    log.debug("Procurando role pelo nome: {}", name);
    RoleType roleType;
    try {
      roleType = RoleType.valueOf(name.trim().toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new NoSuchElementException("Role inválida: " + name);
    }

    Role role = roleRepository.findByName(roleType)
      .orElseThrow(() -> new NoSuchElementException("Role não encontrada: " + name));
    return roleMapper.toResponseDTO(role);
  }

  @Override
  @Transactional(readOnly = true)
  public List<RoleResponseDTO> listRoles() {
    log.info("Listando todas as roles");
    return roleRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
      .map(roleMapper::toResponseDTO)
      .toList();
  }

}
