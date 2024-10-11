package com.nxh.identityservice.dto.request;

import com.nxh.identityservice.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

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

  @DobConstraint(min = 18, message = "INVALID_DOB")
  LocalDate dob;
}
