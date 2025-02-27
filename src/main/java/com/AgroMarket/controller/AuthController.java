package com.AgroMarket.controller;

import com.AgroMarket.dto.UserRegistrationDto;
import com.AgroMarket.service.NewsletterService;
import com.AgroMarket.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final NewsletterService newsletterService;

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new UserRegistrationDto());
    return "register";
  }

  @PostMapping("/register")
  public String registerUser(
      @ModelAttribute("user") @Valid UserRegistrationDto registrationDto,
      BindingResult result,
      @RequestParam(required = false) boolean newsletter,
      RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      return "register";
    }

    try {
      var user = userService.registerNewUser(registrationDto);
      user.setSubscribeToNewsletter(newsletter);
      if (newsletter) {
        newsletterService.subscribe(user.getEmail());
      }
      redirectAttributes.addFlashAttribute("successMessage", "Регистрация успешно завершена!");
      return "redirect:/login";
    } catch (Exception e) {
      result.rejectValue("global", "error.global", e.getMessage());
      return "register";
    }
  }

  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }
}