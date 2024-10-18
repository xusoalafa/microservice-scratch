package com.nxh.identity.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.nxh.identity.dto.request.ApiResponse;
import com.nxh.identity.dto.request.UserCreationRequest;
import com.nxh.identity.dto.request.UserUpdateRequest;
import com.nxh.identity.dto.response.UserResponse;
import com.nxh.identity.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
  @Autowired private UserService userService;

  @PostMapping("/registration")
  ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
    ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(userService.createUser(request));
    return apiResponse;
  }

  @GetMapping
  ApiResponse<List<UserResponse>> getUsers() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    log.info("Username/Id: " + authentication.getName());
    authentication.getAuthorities().forEach(a -> log.info(a.getAuthority()));
    return ApiResponse.<List<UserResponse>>builder().result(userService.getUsers()).build();
  }

  @GetMapping("/{userId}")
  ApiResponse<UserResponse> createUser(@PathVariable String userId) {
    return ApiResponse.<UserResponse>builder().result(userService.getUser(userId)).build();
  }

  @GetMapping("/my-info")
  ApiResponse<UserResponse> getMyInfo() {
    return ApiResponse.<UserResponse>builder().result(userService.getMyInfo()).build();
  }

  @PutMapping("/{userId}")
  ApiResponse<UserResponse> updateUser(
      @RequestBody @Valid UserUpdateRequest request, @PathVariable String userId) {
    return ApiResponse.<UserResponse>builder()
        .result(userService.updateUser(userId, request))
        .build();
  }

  @DeleteMapping("/{userId}")
  ApiResponse<String> deleteUser(@PathVariable String userId) {
    userService.deleteUser(userId);
    return ApiResponse.<String>builder().result("User was deleted").build();
  }
}
