package com.identityaccessdomain.identityprofiles.service.impl;

import com.identityaccessdomain.identityprofiles.domain.entity.Role;
import com.identityaccessdomain.identityprofiles.domain.enums.RoleType;
import com.identityaccessdomain.identityprofiles.dto.request.RoleRequestDTO;
import com.identityaccessdomain.identityprofiles.dto.response.RoleResponseDTO;
import com.identityaccessdomain.identityprofiles.mapping.RoleMapper;
import com.identityaccessdomain.identityprofiles.repositories.RoleRepository;
import com.identityaccessdomain.identityprofiles.service.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Tipo de Teste: Teste com Spring Boot <br>
 * Objetivo: Validar integração entre beans reais e mocks Spring <br>
 * Execução: Um pouco mais lento <br>
 * Escopo: Usa DI real do Spring
 */
@SpringBootTest(classes = RoleServiceImpl.class)
class RoleServiceImplSpringTest {

  @Autowired
  RoleServiceImpl roleService;

  @MockitoBean
  RoleRepository roleRepository;

  @MockitoBean
  RoleMapper roleMapper;

  @MockitoBean
  AuditLogService auditLogService;

  Role role;
  RoleRequestDTO requestDTO;
  RoleResponseDTO responseDTO;

  @BeforeEach
  void setUp() {
    role = Role.builder()
      .id(1L)
      .name(RoleType.ADMIN)
      .description("Administrador do sistema")
      .build();

    requestDTO = RoleRequestDTO.builder()
      .name("ADMIN")
      .description("Administrador do sistema")
      .build();

    responseDTO = RoleResponseDTO.builder()
      .id(1L)
      .name("ADMIN")
      .description("Administrador do sistema")
      .build();
  }

  @Test
  @DisplayName("Deve criar Role com sucesso (Spring Boot)")
  void testCreateRole_Success() {
    // Arrange
    when(roleRepository.findByName(RoleType.ADMIN)).thenReturn(Optional.empty());
    when(roleMapper.toEntity(requestDTO)).thenReturn(role);
    when(roleRepository.save(any(Role.class))).thenReturn(role);
    when(roleMapper.toResponseDTO(role)).thenReturn(responseDTO);

    // Act
    RoleResponseDTO result = roleService.createRole(requestDTO, "adminUser");

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(1L);
    assertThat(result.name()).isEqualTo("ADMIN");
    assertThat(result.description()).isEqualTo("Administrador do sistema");

    verify(roleRepository).findByName(RoleType.ADMIN);
    verify(roleRepository).save(any(Role.class));
    verify(auditLogService).logEvent(
      eq("Role"),
      eq(1L),
      eq("CREATE"),
      eq("adminUser"),
      contains("Role criada")
    );

  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar criar Role já existente")
  void testCreateRole_AlreadyExists() {
    when(roleRepository.findByName(RoleType.ADMIN)).thenReturn(Optional.of(role));

    assertThatThrownBy(() -> roleService.createRole(requestDTO, "adminUser"))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("Role já existe");

    verify(roleRepository, never()).save(any(Role.class));
    verify(auditLogService, never()).logEvent(any(), any(), any(), any(), any());
  }

  @Test
  @DisplayName("Deve lançar exceção se o RoleType for inválido")
  void testCreateRole_InvalidRoleType() {
    var invalidRequest = RoleRequestDTO.builder()
      .name("INVALIDO")
      .description("Perfil inválido")
      .build();

    assertThatThrownBy(() -> roleService.createRole(invalidRequest, "adminUser"))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("Deve retornar Role por nome com sucesso")
  void testGetRoleByName_Success() {
    when(roleRepository.findByName(RoleType.ADMIN)).thenReturn(Optional.of(role));
    when(roleMapper.toResponseDTO(role)).thenReturn(responseDTO);

    RoleResponseDTO result = roleService.getRoleByName("ADMIN");

    assertThat(result).isNotNull();
    assertThat(result.name()).isEqualTo("ADMIN");
    verify(roleRepository).findByName(RoleType.ADMIN);
  }

  @Test
  @DisplayName("Deve lançar exceção se Role não encontrada")
  void testGetRoleByName_NotFound() {
    when(roleRepository.findByName(RoleType.MANAGER)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> roleService.getRoleByName("MANAGER"))
      .isInstanceOf(NoSuchElementException.class)
      .hasMessageContaining("Role não encontrada");

    verify(roleRepository).findByName(RoleType.MANAGER);
  }

  @Test
  @DisplayName("Deve listar todas as Roles com sucesso")
  void testListRoles_Success() {
    List<Role> roles = List.of(
      Role.builder().id(1L).name(RoleType.ADMIN).description("Administrador").build(),
      Role.builder().id(2L).name(RoleType.CUSTOMER).description("Cliente").build()
    );

    var dto1 = RoleResponseDTO.builder()
      .id(1L)
      .name("ADMIN")
      .description("Administrador")
      .build();
    var dto2 = RoleResponseDTO.builder()
      .id(2L)
      .name("CUSTOMER")
      .description("Cliente")
      .build();

    when(roleRepository.findAll()).thenReturn(roles);
    when(roleMapper.toResponseDTO(roles.get(0))).thenReturn(dto1);
    when(roleMapper.toResponseDTO(roles.get(1))).thenReturn(dto2);

    List<RoleResponseDTO> result = roleService.listRoles();

    assertThat(result).hasSize(2);
    assertThat(result).extracting("name").containsExactly("ADMIN", "CUSTOMER");

    verify(roleRepository).findAll();
  }

}