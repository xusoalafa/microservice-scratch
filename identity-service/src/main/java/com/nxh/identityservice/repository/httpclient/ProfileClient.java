package com.nxh.identityservice.repository.httpclient;

import com.nxh.identityservice.configuration.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nxh.identityservice.dto.request.ProfileCreationRequest;
import com.nxh.identityservice.dto.response.UserProfileResponse;

@FeignClient(name = "profile-client", url = "${app.services.profile}", configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
  @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
  UserProfileResponse createProfile(@RequestBody ProfileCreationRequest request);
}
