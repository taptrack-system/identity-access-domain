package com.identityaccessdomain.userservice.infra.search.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "users_docs")
public class UserDocument {

  @Id
  private Long id;

  @NotBlank(message = "O primeiro nome é obrigatório.")
  @Size(min = 3, max = 50, message = "O primeiro nome deve ter entre {min} e {max} caracteres.")
  @Field(type = FieldType.Text)
  private String firstName;

  @NotBlank(message = "O sobrenome é obrigatório.")
  @Size(min = 3, max = 50, message = "O sobrenome deve ter entre {min} e {max} caracteres.")
  @Field(type = FieldType.Text)
  private String lastName;

  @NotBlank(message = "O e-mail é obrigatório.")
  @Email(message = "O e-mail deve ser válido.")
  @Size(max = 150, message = "O e-mail deve ter no máximo {max} caracteres.")
  @Field(type = FieldType.Keyword)
  private String email;

}
