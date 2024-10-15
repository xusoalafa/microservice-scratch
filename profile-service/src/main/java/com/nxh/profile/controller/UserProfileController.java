package com.nxh.profile.controller;

import com.nxh.profile.dto.request.ProfileCreationRequest;
import com.nxh.profile.dto.response.UserProfileReponse;
import com.nxh.profile.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping
    UserProfileReponse createProfile(@RequestBody ProfileCreationRequest request) {
        return userProfileService.createProfile(request);
    }

    @GetMapping("/{profileId}")
    UserProfileReponse getProfile(@PathVariable String profileId) {
        return userProfileService.getProfile(profileId);
    }
}
