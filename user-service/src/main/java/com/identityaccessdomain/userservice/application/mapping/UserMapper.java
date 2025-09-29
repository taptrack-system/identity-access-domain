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

  /**
   * Atualiza uma entidade User existente com base nos dados de um UserRequestDTO.
   * Campos nulos no DTO são ignorados, permitindo atualizações parciais.
   *
   * @param dto  O DTO contendo os dados para atualização.
   * @param user A entidade User a ser atualizada.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromDto(UserRequestDTO dto, @MappingTarget User user);


}
