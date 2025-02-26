package com.AgroMarket.service;

import com.AgroMarket.dto.UserRegistrationDto;
import com.AgroMarket.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
  User registerNewUser(UserRegistrationDto registrationDto);

  boolean isUsernameExists(String username);

  boolean isEmailExists(String email);

  User findByUsername(String username);

  void updateProfile(String username, String firstName, String lastName, String email, String phone);

  void changePassword(String username, String currentPassword, String newPassword, String confirmPassword);

  Page<User> findAll(Pageable pageable);

  long countUsers();
}