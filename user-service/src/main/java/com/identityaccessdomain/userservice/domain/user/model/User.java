package com.identityaccessdomain.userservice.domain.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_tb")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "O primeiro nome é obrigatório.")
  @Size(min = 3, max = 50, message = "O primeiro nome deve ter entre {min} e {max} caracteres.")
  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  @NotBlank(message = "O sobrenome é obrigatório.")
  @Size(min = 3, max = 50, message = "O sobrenome deve ter entre {min} e {max} caracteres.")
  @Column(name = "last_name", nullable = false, length = 150)
  private String lastName;

  @NotBlank(message = "O e-mail é obrigatório.")
  @Email(message = "O e-mail deve ser válido.")
  @Size(max = 150, message = "O e-mail deve ter no máximo {max} caracteres.")
  @Column(name = "email", unique = true, nullable = false, length = 150)
  private String email;

}
