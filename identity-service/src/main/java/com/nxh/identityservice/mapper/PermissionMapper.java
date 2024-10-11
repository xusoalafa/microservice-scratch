package com.nxh.identityservice.mapper;

import org.mapstruct.Mapper;

import com.nxh.identityservice.dto.request.PermissionRequest;
import com.nxh.identityservice.dto.response.PermissionResponse;
import com.nxh.identityservice.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  Permission toPermission(PermissionRequest request);

  PermissionResponse toPermissionResponse(Permission permission);
}
