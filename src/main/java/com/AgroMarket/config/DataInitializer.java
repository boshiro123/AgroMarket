package com.AgroMarket.config;

import com.AgroMarket.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

  @PersistenceContext
  private EntityManager entityManager;

  @Bean
  @Transactional
  public CommandLineRunner initData() {
    return args -> {
      log.info("Начало инициализации данных...");

      // Проверяем, есть ли уже данные в базе
      Long userCount = (Long) entityManager.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();

      if (userCount > 0) {
        log.info("База данных уже содержит данные. Пропускаем инициализацию.");
        return;
      }

      // Создаем роли
      Role adminRole = createRoleIfNotExists("ROLE_ADMIN");
      Role userRole = createRoleIfNotExists("ROLE_USER");

      // Создаем тестового пользователя
      User testUser = User.builder()
          .username("user")
          .email("user@beltrufsmak.com")
          .password("user123")
          .firstName("Тестовый")
          .lastName("Пользователь")
          .phone("+375291234567")
          .address("г. Минск, ул. Примерная, д. 1")
          .subscribeToNewsletter(true)
          .roles(Set.of(userRole))
          .build();

      entityManager.persist(testUser);

      // Создаем тестовую корзину для пользователя
      Cart cart = Cart.builder()
          .user(testUser)
          .totalPrice(BigDecimal.ZERO)
          .build();

      entityManager.persist(cart);

      log.info("Инициализация данных завершена успешно!");
    };
  }

  @Transactional
  private Role createRoleIfNotExists(String roleName) {
    List<Role> roles = entityManager
        .createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
        .setParameter("name", roleName)
        .getResultList();

    if (!roles.isEmpty()) {
      return roles.get(0);
    }

    Role role = Role.builder().name(roleName).build();
    entityManager.persist(role);
    return role;
  }
}