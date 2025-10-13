package com.identityaccessdomain.identityprofiles.mapping;

import com.identityaccessdomain.identityprofiles.domain.entity.Role;
import com.identityaccessdomain.identityprofiles.domain.enums.RoleType;
import com.identityaccessdomain.identityprofiles.dto.request.RoleRequestDTO;
import com.identityaccessdomain.identityprofiles.dto.response.RoleResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
public interface RoleMapper {

  // Converte entity -> dto (RoleType -> String)
  @Mapping(target = "name", expression = "java(role.getName() != null ? role.getName().name() : null)")
  RoleResponseDTO toResponseDTO(Role role);

  // Converte dto -> entity (String -> RoleType) - id ignorado
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", expression = "java(toRoleType(dto.name()))")
  Role toEntity(RoleRequestDTO dto);

  // helper method — MapStruct irá usar metodo estático do mapper se estiver presente
  default RoleType toRoleType(String name) {
    if (name == null) return null;
    try {
      return RoleType.valueOf(name.trim().toUpperCase());
    } catch (IllegalArgumentException ex) {
      // Repassa para ser tratado em service/controller
      throw new IllegalArgumentException("Valor inválido para RoleType: " + name);
    }
  }

}
