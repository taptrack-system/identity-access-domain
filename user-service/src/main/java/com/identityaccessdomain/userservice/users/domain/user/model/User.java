package com.identityaccessdomain.userservice.users.domain.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Getter
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

  @NotBlank(message = "O CPF é obrigatório.")
  @CPF(message = "O CPF deve ser válido.")
  @Column(name = "document_number", unique = true, nullable = false)
  private String documentNumber;

  // Utilize o formato padrão internacional para número de telefone (E.164),
  // definido pela União Internacional de Telecomunicações (UIT)
  // Sinal de mais (+): indica que ´eum número internacional
  // Código do País (CC) ou DDI: Código do País de 1 a 3 dígitos (ex: Brasil 55)
  // Código de Destino Nacional (NDC) ou DDD: Código de área ou código de serviço nacional
  // Número do assinante (SN): O número de telefone individual
  //
  @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "")
  private String phoneNumber;

  private String address;

}
