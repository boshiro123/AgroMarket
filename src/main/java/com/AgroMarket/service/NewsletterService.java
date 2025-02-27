package com.AgroMarket.service;

import com.AgroMarket.models.NewsletterSubscriber;
import com.AgroMarket.repository.NewsletterSubscriberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsletterService {
  private final JavaMailSender mailSender;
  private final NewsletterSubscriberRepository subscriberRepository;

  @Transactional
  public void subscribe(String email) {
    if (!subscriberRepository.existsByEmail(email)) {
      NewsletterSubscriber subscriber = new NewsletterSubscriber();
      subscriber.setEmail(email);
      subscriberRepository.save(subscriber);
    }
  }

  @Transactional
  public void unsubscribe(String email) {
    subscriberRepository.findByEmail(email)
        .ifPresent(subscriberRepository::delete);
  }

  @Transactional(readOnly = true)
  public boolean isSubscribed(String email) {
    return subscriberRepository.existsByEmail(email);
  }

  @Transactional(readOnly = true)
  public void sendNewsletterToAllSubscribers(String subject, String text) throws MessagingException {
    for (NewsletterSubscriber subscriber : subscriberRepository.findAll()) {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setTo(subscriber.getEmail());
      helper.setSubject(subject);
      helper.setText(text, true);

      mailSender.send(message);
    }
  }
}