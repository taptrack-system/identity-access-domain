package com.identityaccessdomain.identityprofiles.service.impl;

import com.identityaccessdomain.identityprofiles.domain.entity.AuditLog;
import com.identityaccessdomain.identityprofiles.dto.response.AuditLogResponseDTO;
import com.identityaccessdomain.identityprofiles.mapping.AuditLogMapper;
import com.identityaccessdomain.identityprofiles.repositories.AuditLogRepository;
import com.identityaccessdomain.identityprofiles.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

  private final AuditLogMapper auditLogMapper;
  private final AuditLogRepository auditLogRepository;

  @Override
  @Transactional
  public void logEvent(String entity, Long entityId, String action, String performedBy, String details) {
    var auditLog = AuditLog.builder()
      .entity(entity)
      .entityId(entityId)
      .action(action)
      .performedBy(performedBy)
      .details(details)
      .build();
    auditLogRepository.save(auditLog);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuditLogResponseDTO> listLogs() {
    return auditLogRepository.findAll().stream()
      .map(auditLogMapper::toResponseDTO)
      .toList();

  }

}
