package com.identityaccessdomain.userservice.model;

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
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "O primeiro nome é obrigatório.")
  @Size(min = 2, max = 50, message = "O primeiro nome deve ter entre 2 e 50 caracteres.")
  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  @NotBlank(message = "O sobrenome é obrigatório.")
  @Size(min = 2, max = 50, message = "O sobrenome deve ter entre 2 e 50 caracteres.")
  @Column(name = "last_name", nullable = false, length = 50)
  private String lastName;


  @NotBlank(message = "O e-mail é obrigatório.")
  @Email(message = "O e-mail deve ser válido.")
  @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
  @Column(name = "email", unique = true, nullable = false, length = 100)
  private String email;

}
