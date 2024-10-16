package com.nxh.profile.configuration;

import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

/** Custom to validate logout token / black list token */
@Component
public class CustomJwtDecoder implements JwtDecoder {
  @Override
  public Jwt decode(String token) throws JwtException {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
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
