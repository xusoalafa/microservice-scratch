package com.nxh.profile.mapper;

import com.nxh.profile.dto.request.ProfileCreationRequest;
import com.nxh.profile.dto.response.UserProfileReponse;
import com.nxh.profile.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileReponse toUserProfileReponse(UserProfile entity);
}
