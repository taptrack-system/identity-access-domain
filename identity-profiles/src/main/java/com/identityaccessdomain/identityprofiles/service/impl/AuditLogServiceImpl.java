package com.identityaccessdomain.identityprofiles.service.impl;

import com.identityaccessdomain.identityprofiles.domain.entity.AuditLog;
import com.identityaccessdomain.identityprofiles.dto.response.AuditLogResponseDTO;
import com.identityaccessdomain.identityprofiles.exception.InternalServerErrorException;
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
    try {
      log.info("Registrando evento de auditoria: entity={}, entityId={}, action={}, performedBy={}",
        entity, entityId, action, performedBy);

      var auditLog = AuditLog.builder()
        .entity(entity)
        .entityId(entityId)
        .action(action)
        .performedBy(performedBy)
        .details(details)
        .build();

      auditLogRepository.save(auditLog);

      log.debug("Evento de auditoria salvo com sucesso: {}", auditLog.getId());
    } catch (Exception ex) {
      log.error("Erro ao registrar evento de auditoria para entity={} com ID={}", entity, entityId, ex);
      throw new InternalServerErrorException("Falha ao registrar evento de auditoria.");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuditLogResponseDTO> listLogs() {
    try {
      log.info("Buscando todos os registros de auditoria...");
      var logs = auditLogRepository.findAll().stream()
        .map(auditLogMapper::toResponseDTO)
        .toList();

      if (logs.isEmpty()) {
        log.warn("Nenhum registro de auditoria encontrado.");
      } else {
        log.debug("Total de registros de auditoria encontrados: {}", logs.size());
      }

      return logs;
    } catch (Exception ex) {
      log.error("Erro ao buscar registros de auditoria", ex);
      throw new InternalServerErrorException("Falha ao buscar registros de auditoria.");
    }
  }

}
