package com.nxh.identityservice.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nxh.identityservice.constant.PredefinedRole;
import com.nxh.identityservice.dto.request.UserCreationRequest;
import com.nxh.identityservice.dto.request.UserUpdateRequest;
import com.nxh.identityservice.dto.response.UserResponse;
import com.nxh.identityservice.entity.Role;
import com.nxh.identityservice.entity.User;
import com.nxh.identityservice.exception.AppException;
import com.nxh.identityservice.exception.ErrorCode;
import com.nxh.identityservice.mapper.UserMapper;
import com.nxh.identityservice.repository.RoleRepository;
import com.nxh.identityservice.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
  UserRepository userRepository;
  RoleRepository roleRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;

  public UserResponse createUser(UserCreationRequest request) {
    User user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    HashSet<Role> roles = new HashSet<>();
    roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
    user.setRoles(roles);
    try {
      user = userRepository.save(user);
    } catch (DataIntegrityViolationException exception) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }
    return userMapper.toUserResponse(user);
  }

  @PreAuthorize("hasRole('ADMIN')")
  // @PreAuthorize("hasAuthority('PERMISSION_VIEW)")
  public List<UserResponse> getUsers() {
    return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
  }

  @PostAuthorize("returnObject.username == authentication.name || hasRole('ADMIN')")
  public UserResponse getUser(String userId) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    return userMapper.toUserResponse(user);
  }

  public UserResponse updateUser(String userId, UserUpdateRequest request) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    userMapper.updateUser(user, request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    var roles = roleRepository.findAllById(request.getRoles());
    user.setRoles(new HashSet<>(roles));
    return userMapper.toUserResponse(userRepository.save(user));
  }

  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }

  public UserResponse getMyInfo() {
    var context = SecurityContextHolder.getContext();
    String name = context.getAuthentication().getName();
    User user =
        userRepository
            .findByUsername(name)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    return userMapper.toUserResponse(user);
  }
}
