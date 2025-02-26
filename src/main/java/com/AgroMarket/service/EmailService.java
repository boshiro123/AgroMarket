package com.AgroMarket.service;

public interface EmailService {
  void sendNewsletterToAllSubscribers(String subject, String text);

  void sendEmail(String to, String subject, String text);
}