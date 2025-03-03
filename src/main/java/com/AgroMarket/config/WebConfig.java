package com.AgroMarket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // Обработка ресурсов из classpath
    registry.addResourceHandler("/uploads/**")
        .addResourceLocations("classpath:/uploads/");

    // Обработка ресурсов из файловой системы
    registry.addResourceHandler("/images/**")
        .addResourceLocations("file:/app/images/")
        .setCachePeriod(3600);
  }
}