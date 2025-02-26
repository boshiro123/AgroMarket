package com.AgroMarket.service.impl;

import com.AgroMarket.service.EmailService;
import com.AgroMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final UserService userService;

  @Override
  @Transactional(readOnly = true)
  public void sendNewsletterToAllSubscribers(String subject, String text) {
    log.info("Начинаем отправку рассылки с темой: {}", subject);
    int pageSize = 100; // Размер страницы
    int currentPage = 0;
    boolean hasMore = true;
    int totalSent = 0;

    try {
      while (hasMore) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        var userPage = userService.findAll(pageable);

        int pageSent = 0;
        for (var user : userPage.getContent()) {
          if (user.isSubscribeToNewsletter()) {
            try {
              sendEmail(user.getEmail(), subject, text);
              pageSent++;
              totalSent++;
            } catch (Exception e) {
              log.error("Ошибка при отправке письма пользователю {}: {}", user.getEmail(), e.getMessage());
            }
          }
        }

        log.info("Отправлено {} писем на странице {}", pageSent, currentPage + 1);
        hasMore = !userPage.isLast();
        currentPage++;
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