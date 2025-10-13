package com.identityaccessdomain.identityprofiles.domain.entity;

import com.identityaccessdomain.identityprofiles.domain.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "O campo 'username' não pode estar em branco")
  @Size(min = 3, max = 50, message = "O 'username' deve ter entre 3 e 50 caracteres")
  @Column(unique = true, nullable = false, length = 50)
  private String username;

  @NotBlank(message = "O campo 'email' não pode estar em branco")
  @Email(message = "Informe um e-mail válido")
  @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
  @Column(unique = true, nullable = false, length = 150)
  private String email;

  @NotBlank(message = "A senha não pode estar em branco")
  @Size(min = 8, max = 255, message = "A senha deve ter entre 8 e 255 caracteres")
  @Column(nullable = false)
  private String password; // armazenar hash (BCrypt/Argon2)

  @NotBlank(message = "O nome completo não pode estar em branco")
  @Size(max = 150, message = "O nome completo deve ter no máximo 150 caracteres")
  @Column(nullable = false, length = 150)
  private String fullName;

  @Builder.Default
  @NotNull(message = "O status do usuário é obrigatório")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private UserStatus status = UserStatus.ACTIVE;

  @Builder.Default
  @Column(updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Builder.Default
  private LocalDateTime updatedAt = LocalDateTime.now();

  // Relação com roles (N:N)
  @Builder.Default
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles = new HashSet<>();

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

}
