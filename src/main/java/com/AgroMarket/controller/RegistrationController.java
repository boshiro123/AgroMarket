package com.AgroMarket.controller;

import com.AgroMarket.dto.UserRegistrationDto;
import com.AgroMarket.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

  private final UserService userService;

  @GetMapping
  public String showRegistrationForm(Model model) {
    if (!model.containsAttribute("user")) {
      model.addAttribute("user", new UserRegistrationDto());
    }
    return "register";
  }

  @PostMapping
  public String registerUser(
      @ModelAttribute("user") @Valid UserRegistrationDto registrationDto,
      BindingResult result,
      RedirectAttributes redirectAttributes) {

    // Проверяем совпадение паролей
    if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
      result.rejectValue("confirmPassword", "error.user", "Пароли не совпадают");
    }

    // Проверяем уникальность username
    if (userService.isUsernameExists(registrationDto.getUsername())) {
      result.rejectValue("username", "error.user", "Пользователь с таким именем уже существует");
    }

    // Проверяем уникальность email
    if (userService.isEmailExists(registrationDto.getEmail())) {
      result.rejectValue("email", "error.user", "Пользователь с таким email уже существует");
    }

    if (result.hasErrors()) {
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
      redirectAttributes.addFlashAttribute("user", registrationDto);
      return "redirect:/register";
    }

    try {
      userService.registerNewUser(registrationDto);
      redirectAttributes.addFlashAttribute("successMessage",
          "Регистрация успешно завершена. Пожалуйста, войдите в систему.");
      return "redirect:/login";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Произошла ошибка при регистрации: " + e.getMessage());
      redirectAttributes.addFlashAttribute("user", registrationDto);
      return "redirect:/register";
    }
  }
}