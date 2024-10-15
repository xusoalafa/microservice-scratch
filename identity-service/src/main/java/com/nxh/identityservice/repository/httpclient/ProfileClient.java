package com.nxh.identityservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nxh.identityservice.dto.request.ProfileCreationRequest;
import com.nxh.identityservice.dto.response.UserProfileResponse;

@FeignClient(name = "profile-client", url = "${app.services.profile}")
public interface ProfileClient {
  @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
  UserProfileResponse createProfile(@RequestBody ProfileCreationRequest request);
}
