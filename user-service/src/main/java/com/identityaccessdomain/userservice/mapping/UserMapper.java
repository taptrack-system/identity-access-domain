package com.identityaccessdomain.userservice.mapping;

import com.identityaccessdomain.userservice.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  User requestDtoToEntity(UserRequestDTO requestDTO);

  UserResponseDTO entityToResponseDto(User user);

}
