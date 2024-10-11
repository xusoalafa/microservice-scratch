package com.nxh.identityservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  String username;
  String password;
  String firstName;
  LocalDate dob;
  String lastName;
  @ManyToMany
  Set<Role> roles;
}
