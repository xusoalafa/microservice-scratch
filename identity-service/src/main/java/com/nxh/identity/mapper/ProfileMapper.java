package com.nxh.identity.mapper;

import org.mapstruct.Mapper;

import com.nxh.identity.dto.request.ProfileCreationRequest;
import com.nxh.identity.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
  ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
