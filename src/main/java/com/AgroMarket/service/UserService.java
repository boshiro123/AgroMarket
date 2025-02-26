package com.AgroMarket.service;

import com.AgroMarket.dto.UserRegistrationDto;
import com.AgroMarket.models.User;

public interface UserService {
  User registerNewUser(UserRegistrationDto registrationDto);

  boolean isUsernameExists(String username);

  boolean isEmailExists(String email);
}