package com.AgroMarket.repository;

import com.AgroMarket.models.NewsletterSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NewsletterSubscriberRepository extends JpaRepository<NewsletterSubscriber, Long> {
  boolean existsByEmail(String email);

  Optional<NewsletterSubscriber> findByEmail(String email);
}