package com.identityaccessdomain.userservice.clients.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 30/09/2025
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Nome é obrigatório")
  @Column(nullable = false)
  private String name;

  @NotBlank(message = "Telefone é obrigatório")
  @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Telefone inválido")
  @Column(nullable = false)
  private String phone;

  @NotBlank(message = "E-mail é obrigatório")
  @Email(message = "E-mail inválido")
  @Column(nullable = false, unique = true)
  private String email;

}
