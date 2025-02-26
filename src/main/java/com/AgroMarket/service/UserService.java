package com.AgroMarket.service;

import com.AgroMarket.dto.UserRegistrationDto;
import com.AgroMarket.models.User;

public interface UserService {
  User registerNewUser(UserRegistrationDto registrationDto);

  boolean isUsernameExists(String username);

  boolean isEmailExists(String email);

  User findByUsername(String username);

  void updateProfile(String username, String firstName, String lastName, String email, String phone);

  void changePassword(String username, String currentPassword, String newPassword, String confirmPassword);
}