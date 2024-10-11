package com.nxh.identityservice.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nxh.identityservice.dto.request.AuthenticationRequest;
import com.nxh.identityservice.dto.request.IntrospectRequest;
import com.nxh.identityservice.dto.request.LogoutRequest;
import com.nxh.identityservice.dto.request.RefreshTokenRequest;
import com.nxh.identityservice.dto.response.AuthenticationResponse;
import com.nxh.identityservice.dto.response.IntrospectResponse;
import com.nxh.identityservice.entity.InvalidatedToken;
import com.nxh.identityservice.entity.User;
import com.nxh.identityservice.exception.AppException;
import com.nxh.identityservice.exception.ErrorCode;
import com.nxh.identityservice.repository.InvalidatedTokenRepository;
import com.nxh.identityservice.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
  UserRepository userRepository;
  InvalidatedTokenRepository invalidatedTokenRepository;

  @NonFinal
  @Value("${jwt.signerKey}")
  protected String SIGNER_KEY;

  @NonFinal
  @Value("${jwt.valid-duration}")
  protected long VALID_DURATION;

  @NonFinal
  @Value("${jwt.refreshable-duration}")
  protected long REFRESHABLE_DURATION;

  public void logout(LogoutRequest request) throws JOSEException, ParseException {
    try {
      SignedJWT signedJWT = verifyToken(request.getToken(), true);
      String jit = signedJWT.getJWTClaimsSet().getJWTID();
      Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();

      InvalidatedToken invalidatedToken =
          InvalidatedToken.builder().id(jit).expireTime(expireTime).build();
      invalidatedTokenRepository.save(invalidatedToken);
    } catch (AppException exception) {
      log.info("Token already expired");
    }
  }

  public AuthenticationResponse refreshToken(RefreshTokenRequest request)
      throws ParseException, JOSEException {
    SignedJWT signedJWT = verifyToken(request.getToken(), true);
    String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
    Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();
    InvalidatedToken invalidatedToken =
        InvalidatedToken.builder().id(jwtId).expireTime(expireTime).build();
    invalidatedTokenRepository.save(invalidatedToken);
    String username = signedJWT.getJWTClaimsSet().getSubject();
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
    String token = generateToken(user);
    return AuthenticationResponse.builder().token(token).authenticated(true).build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    var user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

    if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

    var token = generateToken(user);

    return AuthenticationResponse.builder().token(token).authenticated(true).build();
  }

  private String generateToken(User user) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

    JWTClaimsSet jwtClaimsSet =
        new JWTClaimsSet.Builder()
            .subject(user.getUsername())
            .issuer("nxh.com")
            .issueTime(new Date())
            .expirationTime(
                new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
            .jwtID(UUID.randomUUID().toString())
            .claim("scope", buildScope(user))
            .build();

    Payload payload = new Payload(jwtClaimsSet.toJSONObject());

    JWSObject jwsObject = new JWSObject(header, payload);

    try {
      jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error("Cannot create token", e);
      throw new RuntimeException(e);
    }
  }

  private String buildScope(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");

    if (!CollectionUtils.isEmpty(user.getRoles()))
      user.getRoles()
          .forEach(
              role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                  role.getPermissions()
                      .forEach(permission -> stringJoiner.add(permission.getName()));
              });

    return stringJoiner.toString();
  }

  public IntrospectResponse introspect(IntrospectRequest request)
      throws JOSEException, ParseException {
    boolean isValid = true;
    try {
      verifyToken(request.getToken(), false);
    } catch (AppException ex) {
      isValid = false;
    }
    return IntrospectResponse.builder().valid(isValid).build();
  }

  private SignedJWT verifyToken(String token, boolean isRefresh)
      throws JOSEException, ParseException {
    JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
    SignedJWT signedJWT = SignedJWT.parse(token);
    Date expiredTime =
        isRefresh
            ? new Date(
                signedJWT
                    .getJWTClaimsSet()
                    .getIssueTime()
                    .toInstant()
                    .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                    .toEpochMilli())
            : signedJWT.getJWTClaimsSet().getExpirationTime();
    var verified = signedJWT.verify(jwsVerifier);
    if (!(verified && expiredTime.after(new Date()))
        || invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
    return signedJWT;
  }
}
