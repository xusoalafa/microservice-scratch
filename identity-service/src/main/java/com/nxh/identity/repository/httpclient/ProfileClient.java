package com.nxh.identity.repository.httpclient;

import com.nxh.identity.dto.request.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nxh.identity.configuration.AuthenticationRequestInterceptor;
import com.nxh.identity.dto.request.ProfileCreationRequest;
import com.nxh.identity.dto.response.UserProfileResponse;

@FeignClient(
    name = "profile-client",
    url = "${app.services.profile}",
    configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
  @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
  ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request);
}
