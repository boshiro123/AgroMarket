package com.AgroMarket.controller;

import com.AgroMarket.models.User;
import com.AgroMarket.models.Order;
import com.AgroMarket.service.UserService;
import com.AgroMarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

  private final UserService userService;
  private final OrderService orderService;

  @GetMapping
  public String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    User user = userService.findByUsername(userDetails.getUsername());
    Page<Order> orders = orderService.findByUsername(
        userDetails.getUsername(),
        PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")));
    model.addAttribute("user", user);
    model.addAttribute("orders", orders.getContent());
    return "profile/index";
  }

  @GetMapping("/edit")
  public String showEditForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    User user = userService.findByUsername(userDetails.getUsername());
    model.addAttribute("user", user);
    return "profile/edit";
  }

  @PostMapping("/edit")
  public String updateProfile(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestParam String firstName,
      @RequestParam String lastName,
      @RequestParam String email,
      @RequestParam String phone,
      RedirectAttributes redirectAttributes) {
    try {
      userService.updateProfile(
          userDetails.getUsername(),
          firstName,
          lastName,
          email,
          phone);
      redirectAttributes.addFlashAttribute("successMessage", "Профиль успешно обновлен");
      return "redirect:/profile";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return "redirect:/profile/edit";
    }
  }

  @PostMapping("/change-password")
  public String changePassword(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestParam String currentPassword,
      @RequestParam String newPassword,
      @RequestParam String confirmPassword,
      RedirectAttributes redirectAttributes) {
    try {
      userService.changePassword(
          userDetails.getUsername(),
          currentPassword,
          newPassword,
          confirmPassword);
      redirectAttributes.addFlashAttribute("successMessage", "Пароль успешно изменен");
      return "redirect:/profile";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return "redirect:/profile";
    }
  }
}