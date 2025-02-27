package com.AgroMarket.controller;

import com.AgroMarket.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/newsletter")
@RequiredArgsConstructor
public class NewsletterController {
  private final NewsletterService newsletterService;

  @PostMapping("/subscribe")
  public ResponseEntity<String> subscribe(@RequestParam String email) {
    try {
      newsletterService.subscribe(email);
      return ResponseEntity.ok("Вы успешно подписались на рассылку!");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Ошибка при подписке на рассылку");
    }
  }

  @PostMapping("/unsubscribe")
  public ResponseEntity<String> unsubscribe(@RequestParam(required = false) String email,
      Authentication authentication) {
    try {
      String emailToUnsubscribe = email;
      if (email == null && authentication != null && authentication.isAuthenticated()) {
        emailToUnsubscribe = authentication.getName();
      }

      if (emailToUnsubscribe != null) {
        newsletterService.unsubscribe(emailToUnsubscribe);
        return ResponseEntity.ok("Вы успешно отписались от рассылки!");
      }

      return ResponseEntity.badRequest().body("Email не указан");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Ошибка при отписке от рассылки");
    }
  }

  @GetMapping("/status")
  public ResponseEntity<Map<String, Boolean>> getSubscriptionStatus(
      @RequestParam(required = false) String email,
      Authentication authentication) {
    try {
      String emailToCheck = email;
      if (email == null && authentication != null && authentication.isAuthenticated()) {
        emailToCheck = authentication.getName();
      }

      if (emailToCheck != null) {
        boolean isSubscribed = newsletterService.isSubscribed(emailToCheck);
        return ResponseEntity.ok(Map.of("subscribed", isSubscribed));
      }

      return ResponseEntity.ok(Map.of("subscribed", false));
    } catch (Exception e) {
      return ResponseEntity.ok(Map.of("subscribed", false));
    }
  }
}