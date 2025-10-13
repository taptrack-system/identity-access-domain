package com.identityaccessdomain.identityprofiles.domain.entity;

import com.identityaccessdomain.identityprofiles.domain.enums.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "O nome do perfil é obrigatório")
  @Enumerated(EnumType.STRING)
  @Column(name = "name", unique = true, nullable = false, length = 50)
  private RoleType name;

  @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
  @Column(length = 255, name = "description")
  private String description;

}
