package com.nxh.identity.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.nxh.identity.dto.request.*;
import com.nxh.identity.dto.response.AuthenticationResponse;
import com.nxh.identity.dto.response.IntrospectResponse;
import com.nxh.identity.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
  AuthenticationService authenticationService;

  @PostMapping("/token")
  ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    var result = authenticationService.authenticate(request);
    return ApiResponse.<AuthenticationResponse>builder().result(result).build();
  }

  @PostMapping("/introspect")
  ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
      throws ParseException, JOSEException {
    var result = authenticationService.introspect(request);
    return ApiResponse.<IntrospectResponse>builder().result(result).build();
  }

  @PostMapping("/logout")
  ApiResponse<Void> logout(@RequestBody LogoutRequest request)
      throws ParseException, JOSEException {
    authenticationService.logout(request);
    return ApiResponse.<Void>builder().build();
  }

  @PostMapping("/refresh")
  ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request)
      throws ParseException, JOSEException {
    var result = authenticationService.refreshToken(request);
    return ApiResponse.<AuthenticationResponse>builder().result(result).build();
  }
}
