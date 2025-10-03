package com.identityaccessdomain.identityprofiles.service;

import com.identityaccessdomain.identityprofiles.dto.response.AuditLogResponseDTO;

import java.util.List;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
public interface AuditLogService {

  void logEvent(String entity, Long entityId, String action, String performedBy, String details);

  List<AuditLogResponseDTO> listLogs();

}
