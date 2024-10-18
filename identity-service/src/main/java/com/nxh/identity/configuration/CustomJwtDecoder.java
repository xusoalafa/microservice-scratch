package com.nxh.identity.configuration;

import java.text.ParseException;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.SignedJWT;

/** Custom to validate logout token / black list token */
@Component
public class CustomJwtDecoder implements JwtDecoder {
  @Override
  public Jwt decode(String token) throws JwtException {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      // This will not verify the token is valid or not since we don't setup SecretKey and call
      // introspect() method to make verify token
      // we only leverage the security to reuse the remaining function like @PreAuthorize() and
      // @PostAuthorize()
      return new Jwt(
          token,
          signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
          signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
          signedJWT.getHeader().toJSONObject(),
          signedJWT.getJWTClaimsSet().getClaims());

    } catch (ParseException e) {
      throw new JwtException("Invalid token");
    }
  }
}
