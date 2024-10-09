package com.nxh.identityservice.mapper;

import com.nxh.identityservice.dto.request.UserCreationRequest;
import com.nxh.identityservice.dto.response.UserResponse;
import com.nxh.identityservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserCreationRequest request);
  UserResponse toUserResponse(User user);
}
