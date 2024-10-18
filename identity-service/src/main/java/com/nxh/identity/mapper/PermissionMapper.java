package com.nxh.identity.mapper;

import org.mapstruct.Mapper;

import com.nxh.identity.dto.request.PermissionRequest;
import com.nxh.identity.dto.response.PermissionResponse;
import com.nxh.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  Permission toPermission(PermissionRequest request);

  PermissionResponse toPermissionResponse(Permission permission);
}
