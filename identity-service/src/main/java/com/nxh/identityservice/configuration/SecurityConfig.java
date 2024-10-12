package com.nxh.identityservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private final String[] H2_ENDPOINT = {"/h2-console/**"};

  private final String[] PUBLIC_ENDPOINT = {
    "/users", "/auth/token", "auth/verify", "/auth/logout", "/auth/refresh"
  };

  @Value("${jwt.signerKey}")
  protected String SIGNER_KEY;

  @Autowired private CustomJwtDecoder customJwtDecoder;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeHttpRequests(
        request ->
            request
                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT)
                .permitAll()
                .requestMatchers(H2_ENDPOINT)
                .permitAll()
                // .requestMatchers(HttpMethod.GET, "/users")
                // .hasAuthority("ROLE_ADMIN")
                // .hasRole(Role.ADMIN.name())
                .anyRequest()
                .authenticated());
    httpSecurity.oauth2ResourceServer(
        oauth2 ->
            oauth2
                .jwt(
                    jwtConfigurer ->
                        jwtConfigurer
                            .decoder(customJwtDecoder)
                            .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    // setup to view h2-console
    httpSecurity.headers(
        httpSecurityHeadersConfigurer ->
            httpSecurityHeadersConfigurer.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::disable));
    return httpSecurity.build();
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
        new JwtGrantedAuthoritiesConverter();
    // change prefix default SCOPE_ to ROLE_
    // jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
    // We actually included custom prefix ROLE_ in the value of role to distinguish role and
    // permission, can refer to AuthenticationService.buildScope() for more details.
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }

  // we create a CustomJwtDecoder, so we will not use this bean in jwtConfigurer.decoder()
  //  @Bean
  //  JwtDecoder jwtEncoder() {
  //    SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
  //    return
  // NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
  //  }

  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();

    corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.addAllowedHeader("*");

    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

    return new CorsFilter(urlBasedCorsConfigurationSource);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }
}
