package com.nxh.identityservice.mapper;

import com.nxh.identityservice.dto.request.RoleRequest;
import com.nxh.identityservice.dto.response.RoleResponse;
import com.nxh.identityservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  @Mapping(target = "permissions", ignore = true)
  Role toRole(RoleRequest request);

  RoleResponse toRoleResponse(Role role);
}
