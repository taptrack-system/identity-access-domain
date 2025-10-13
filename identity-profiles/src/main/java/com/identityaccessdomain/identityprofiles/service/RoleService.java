package com.identityaccessdomain.identityprofiles.service;

import com.identityaccessdomain.identityprofiles.dto.request.RoleRequestDTO;
import com.identityaccessdomain.identityprofiles.dto.response.RoleResponseDTO;

import java.util.List;

/**
 * identity-profiles
 *
 * @author Juliane Maran
 * @since 02/10/2025
 */
public interface RoleService {

  RoleResponseDTO createRole(RoleRequestDTO requestDTO, String performedBy);

  RoleResponseDTO getRoleByName(String name);

  List<RoleResponseDTO> listRoles();

}
