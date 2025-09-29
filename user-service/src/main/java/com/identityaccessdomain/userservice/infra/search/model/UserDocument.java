package com.identityaccessdomain.userservice.infra.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "users_docs")
public class UserDocument {

  @Id
  private Long id;

  @Field(type = FieldType.Text)
  private String firstName;

  @Field(type = FieldType.Text)
  private String lastName;

  @Field(type = FieldType.Keyword)
  private String email;

}
