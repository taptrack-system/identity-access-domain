package com.identityaccessdomain.identityprofiles.mapping;

import com.identityaccessdomain.identityprofiles.domain.entity.Role;
import com.identityaccessdomain.identityprofiles.domain.entity.User;
import com.identityaccessdomain.identityprofiles.domain.enums.RoleType;
import com.identityaccessdomain.identityprofiles.domain.enums.UserStatus;
import com.identityaccessdomain.identityprofiles.dto.request.UserRequestDTO;
import com.identityaccessdomain.identityprofiles.dto.response.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

  private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

  @Test
  void shouldMapUserToResponseDTO() {
    var role = Role.builder()
      .id(1L)
      .name(RoleType.ADMIN)
      .description("Administrador do sistema")
      .build();

    var user = User.builder()
      .id(10L)
      .username("jdoe")
      .email("jdoe@example.com")
      .password("hashedpass")
      .fullName("John Doe")
      .status(UserStatus.ACTIVE)
      .createdAt(LocalDateTime.now().minusDays(1))
      .updatedAt(LocalDateTime.now())
      .roles(Set.of(role))
      .build();

    UserResponseDTO responseDTO = mapper.toResponseDTO(user);

    assertThat(responseDTO.id()).isEqualTo(10L);
    assertThat(responseDTO.username()).isEqualTo("jdoe");
    assertThat(responseDTO.email()).isEqualTo("jdoe@example.com");
    assertThat(responseDTO.fullName()).isEqualTo("John Doe");
    assertThat(responseDTO.status()).isEqualTo(UserStatus.ACTIVE);
    assertThat(responseDTO.roles()).containsExactly("ADMIN");

  }

  @Test
  void shouldMapRequestDTOToUserWithDefaultActiveStatus() {
    UserRequestDTO request = UserRequestDTO.builder()
      .username("maria")
      .email("maria@example.com")
      .password("strongpass")
      .fullName("Maria Silva")
      .roles(Set.of("CUSTOMER"))
      .build();

    User user = mapper.toEntity(request);

    assertThat(user.getId()).isNull(); // deve ser gerado pelo banco
    assertThat(user.getUsername()).isEqualTo("maria");
    assertThat(user.getEmail()).isEqualTo("maria@example.com");
    assertThat(user.getPassword()).isEqualTo("strongpass");
    assertThat(user.getFullName()).isEqualTo("Maria Silva");
    assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE); // ✅ default
    assertThat(user.getRoles()).isEmpty(); // será tratado no service
  }

}