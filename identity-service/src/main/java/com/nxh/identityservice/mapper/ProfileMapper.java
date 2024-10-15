package com.nxh.identityservice.mapper;

import org.mapstruct.Mapper;

import com.nxh.identityservice.dto.request.ProfileCreationRequest;
import com.nxh.identityservice.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
  ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
