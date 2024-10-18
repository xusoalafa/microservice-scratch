package com.nxh.identity.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.nxh.identity.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
  String password;
  String firstName;
  String lastName;

  @DobConstraint(min = 18, message = "INVALID_DOB")
  LocalDate dob;

  Set<String> roles;
}
