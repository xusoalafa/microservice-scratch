package com.nxh.profile.service;

import com.nxh.profile.dto.request.ProfileCreationRequest;
import com.nxh.profile.dto.response.UserProfileResponse;
import com.nxh.profile.entity.UserProfile;
import com.nxh.profile.mapper.UserProfileMapper;
import com.nxh.profile.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
  UserProfileRepository userProfileRepository;
  UserProfileMapper userProfileMapper;

  public UserProfileResponse createProfile(ProfileCreationRequest request) {
    UserProfile userProfile = userProfileMapper.toUserProfile(request);
    userProfile = userProfileRepository.save(userProfile);

    return userProfileMapper.toUserProfileReponse(userProfile);
  }

  public UserProfileResponse getProfile(String id) {
    UserProfile userProfile =
        userProfileRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Profile not found"));

    return userProfileMapper.toUserProfileReponse(userProfile);
  }

  public List<UserProfileResponse> getAllProfiles() {
    return userProfileRepository.findAll().stream()
        .map(userProfileMapper::toUserProfileReponse)
        .collect(Collectors.toList());
  }
}
