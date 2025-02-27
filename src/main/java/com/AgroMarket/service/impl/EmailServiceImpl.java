package com.AgroMarket.service.impl;

import com.AgroMarket.service.EmailService;
import com.AgroMarket.repository.NewsletterSubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final NewsletterSubscriberRepository subscriberRepository;

  @Override
  @Transactional(readOnly = true)
  public void sendNewsletterToAllSubscribers(String subject, String text) {
    log.info("Начинаем отправку рассылки с темой: {}", subject);
    int totalSent = 0;

    try {
      var subscribers = subscriberRepository.findAll();
      for (var subscriber : subscribers) {
        try {
          sendEmail(subscriber.getEmail(), subject, text);
          totalSent++;
        } catch (Exception e) {
          log.error("Ошибка при отправке письма подписчику {}: {}", subscriber.getEmail(), e.getMessage());
        }
      }
      log.info("Рассылка завершена. Всего отправлено {} писем", totalSent);
    } catch (Exception e) {
      log.error("Ошибка при выполнении рассылки: {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public void sendEmail(String to, String subject, String text) {
    log.info("Отправка письма на адрес: {}", to);
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(to);
      message.setSubject(subject);
      message.setText(text);
      mailSender.send(message);
      log.info("Письмо успешно отправлено на адрес: {}", to);
    } catch (Exception e) {
      log.error("Ошибка при отправке письма на адрес {}: {}", to, e.getMessage());
      throw e;
    }
  }
}