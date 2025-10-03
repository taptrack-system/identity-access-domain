package com.identityaccessdomain.identityprofiles.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

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
@Table(name = "audit_log")
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "O campo 'entity' é obrigatório")
  @Column(nullable = false, length = 100)
  private String entity;

  @NotNull(message = "O campo 'entityId' é obrigatório")
  @Column(nullable = false)
  private Long entityId;

  @NotBlank(message = "O campo 'action' é obrigatório")
  @Column(nullable = false, length = 20)
  private String action; // CREATE, UPDATE, DELETE, LOGIN

  @NotBlank(message = "O campo 'performedBy' é obrigatório")
  @Column(nullable = false, length = 100)
  private String performedBy;

  @Builder.Default
  @Column(nullable = false)
  private LocalDateTime timestamp = LocalDateTime.now();

  @Lob
  private String details; // JSON com mudanças

}
