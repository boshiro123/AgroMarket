package com.AgroMarket.controller;

import com.AgroMarket.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/newsletter")
@RequiredArgsConstructor
public class AdminNewsletterController {

  private final EmailService emailService;

  @GetMapping
  public String showNewsletterForm(Model model) {
    model.addAttribute("currentPath", "/admin/newsletter");
    return "admin/newsletter";
  }

  @PostMapping("/send")
  public String sendNewsletter(
      @RequestParam String subject,
      @RequestParam String text,
      RedirectAttributes redirectAttributes) {
    try {
      emailService.sendNewsletterToAllSubscribers(subject, text);
      redirectAttributes.addFlashAttribute("successMessage", "Рассылка успешно отправлена");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при отправке рассылки: " + e.getMessage());
    }
    return "redirect:/admin/newsletter";
  }
}