package com.identityaccessdomain.identityprofiles.mapping;

import com.identityaccessdomain.identityprofiles.domain.entity.AuditLog;
import com.identityaccessdomain.identityprofiles.dto.response.AuditLogResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuditLogMapper {

  AuditLogResponseDTO toResponseDTO(AuditLog auditLog);

}
