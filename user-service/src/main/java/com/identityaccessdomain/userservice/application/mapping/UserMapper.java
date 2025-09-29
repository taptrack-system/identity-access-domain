package com.identityaccessdomain.userservice.application.mapping;

import com.identityaccessdomain.userservice.api.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.api.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.domain.user.model.User;
import com.identityaccessdomain.userservice.infra.search.model.UserDocument;
import org.mapstruct.*;

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

  UserDocument entityToDocument(User user);

  UserResponseDTO documentToResponseDto(UserDocument document);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // Isso ignora campos nulos no DTO
  void updateEntityFromDto(UserRequestDTO dto, @MappingTarget User entity);


}
