package com.nxh.identityservice.service;

import com.nxh.identityservice.dto.request.RoleRequest;
import com.nxh.identityservice.dto.response.RoleResponse;
import com.nxh.identityservice.mapper.RoleMapper;
import com.nxh.identityservice.repository.PermissionRepository;
import com.nxh.identityservice.repository.RoleRepository;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
  RoleRepository roleRepository;
  PermissionRepository permissionRepository;
  RoleMapper roleMapper;

  public RoleResponse create(RoleRequest request) {
    var role = roleMapper.toRole(request);

    var permissions = permissionRepository.findAllById(request.getPermissions());
    role.setPermissions(new HashSet<>(permissions));

    role = roleRepository.save(role);
    return roleMapper.toRoleResponse(role);
  }

  public List<RoleResponse> getAll() {
    return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
  }

  public void delete(String role) {
    roleRepository.deleteById(role);
  }
}
