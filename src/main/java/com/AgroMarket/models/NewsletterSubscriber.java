package com.AgroMarket.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "newsletter_subscribers")
public class NewsletterSubscriber {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private LocalDateTime subscribedAt;

  @PrePersist
  protected void onCreate() {
    subscribedAt = LocalDateTime.now();
  }
}