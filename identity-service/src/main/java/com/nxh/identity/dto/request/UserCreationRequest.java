package com.nxh.identity.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.nxh.identity.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
  @Size(min = 4, message = "USERNAME_INVALID")
  String username;

  @Size(min = 6, message = "INVALID_PASSWORD")
  String password;

  String firstName;
  String lastName;
  @Email(message = "INVALID_EMAIL")
  @NotBlank(message = "EMAIL_IS_REQUIRED")
  String email;
  @DobConstraint(min = 18, message = "INVALID_DOB")
  LocalDate dob;

  String city;
}
