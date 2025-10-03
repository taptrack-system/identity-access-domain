package com.identityaccessdomain.identityprofiles.mapping;

import com.identityaccessdomain.identityprofiles.domain.entity.Role;
import com.identityaccessdomain.identityprofiles.domain.entity.User;
import com.identityaccessdomain.identityprofiles.dto.request.UserRequestDTO;
import com.identityaccessdomain.identityprofiles.dto.response.UserResponseDTO;
import org.mapstruct.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
@Mapper(
  uses = {RoleMapper.class},
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.IGNORE, // Ignora campos não mapeados sem gerar warning
  imports = Collection.class
)
public interface UserMapper {

  @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleNames")
  UserResponseDTO toResponseDTO(User user);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "roles", ignore = true)
  User toEntity(UserRequestDTO dto);

  @Named("mapRoleNames")
  default Set<String> mapRoleNames(Set<Role> roles) {
    return roles == null ? Set.of() :
      roles.stream().map(r -> r.getName().name()).collect(Collectors.toSet());
  }

}
