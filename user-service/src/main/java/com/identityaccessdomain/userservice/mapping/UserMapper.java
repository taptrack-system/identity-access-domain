package com.identityaccessdomain.userservice.mapping;

import com.identityaccessdomain.userservice.dto.UserRequestDTO;
import com.identityaccessdomain.userservice.dto.UserResponseDTO;
import com.identityaccessdomain.userservice.model.User;
import org.mapstruct.Mapper;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  User requestDtoToEntity(UserRequestDTO requestDTO);

  UserResponseDTO entityToResponseDto(User user);

}
