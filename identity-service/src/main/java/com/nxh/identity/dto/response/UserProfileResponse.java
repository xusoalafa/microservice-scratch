package com.nxh.identity.dto.response;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
  String id;
  String userId;
  String email;
  String firstName;
  String lastName;
  LocalDate dob;
  String city;
}
