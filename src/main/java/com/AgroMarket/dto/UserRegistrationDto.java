package com.AgroMarket.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {

  @NotBlank(message = "Имя пользователя обязательно")
  @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
  private String username;

  @NotBlank(message = "Email обязателен")
  @Email(message = "Некорректный формат email")
  private String email;

  @NotBlank(message = "Пароль обязателен")
  @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
  private String password;

  @NotBlank(message = "Подтверждение пароля обязательно")
  private String confirmPassword;

  @NotBlank(message = "Имя обязательно")
  private String firstName;

  @NotBlank(message = "Фамилия обязательна")
  private String lastName;

  private String phone;
  private String address;
  private String global;
}