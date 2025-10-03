package com.identityaccessdomain.identityprofiles.controllers;

import com.identityaccessdomain.identityprofiles.dto.response.AuditLogResponseDTO;
import com.identityaccessdomain.identityprofiles.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Slf4j
@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

  private final AuditLogService auditLogService;

  @GetMapping
  public ResponseEntity<List<AuditLogResponseDTO>> listLogs() {
    return ResponseEntity.ok(auditLogService.listLogs());
  }

}
