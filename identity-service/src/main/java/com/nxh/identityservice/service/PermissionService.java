package com.nxh.identityservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nxh.identityservice.dto.request.PermissionRequest;
import com.nxh.identityservice.dto.response.PermissionResponse;
import com.nxh.identityservice.entity.Permission;
import com.nxh.identityservice.mapper.PermissionMapper;
import com.nxh.identityservice.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
  PermissionRepository permissionRepository;
  PermissionMapper permissionMapper;

  public PermissionResponse create(PermissionRequest request) {
    Permission permission = permissionMapper.toPermission(request);
    permission = permissionRepository.save(permission);
    return permissionMapper.toPermissionResponse(permission);
  }

  public List<PermissionResponse> getAll() {
    var permissions = permissionRepository.findAll();
    return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
  }

  public void delete(String permission) {
    permissionRepository.deleteById(permission);
  }
}
