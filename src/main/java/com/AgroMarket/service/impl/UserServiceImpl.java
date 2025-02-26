package com.AgroMarket.service.impl;

import com.AgroMarket.dto.UserRegistrationDto;
import com.AgroMarket.mapper.UserMapper;
import com.AgroMarket.models.Cart;
import com.AgroMarket.models.Role;
import com.AgroMarket.models.User;
import com.AgroMarket.repository.RoleRepository;
import com.AgroMarket.repository.UserRepository;
import com.AgroMarket.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public User registerNewUser(UserRegistrationDto registrationDto) {
    // Проверяем, что пароли совпадают
    if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
      throw new IllegalArgumentException("Пароли не совпадают");
    }

    // Проверяем уникальность username и email
    if (isUsernameExists(registrationDto.getUsername())) {
      throw new IllegalArgumentException("Пользователь с таким именем уже существует");
    }
    if (isEmailExists(registrationDto.getEmail())) {
      throw new IllegalArgumentException("Пользователь с таким email уже существует");
    }

    // Преобразуем DTO в сущность
    User user = userMapper.toUser(registrationDto);

    // Устанавливаем зашифрованный пароль
    user.setPassword(registrationDto.getPassword());

    // Получаем роль USER из базы данных
    Role userRole = roleRepository.findByName("ROLE_USER")
        .orElseThrow(() -> new IllegalStateException("Роль ROLE_USER не найдена в базе данных"));

    // Устанавливаем роль пользователю
    Set<Role> roles = new HashSet<>();
    roles.add(userRole);
    user.setRoles(roles);

    // Сохраняем пользователя
    user = userRepository.save(user);

    // Создаем корзину для пользователя
    Cart cart = Cart.builder()
        .user(user)
        .totalPrice(BigDecimal.ZERO)
        .build();

    // Сохраняем корзину (это произойдет автоматически благодаря каскадному
    // сохранению)
    user.setCart(cart);

    return user;
  }

  @Override
  public boolean isUsernameExists(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public boolean isEmailExists(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
  }

  @Override
  @Transactional
  public void updateProfile(String username, String firstName, String lastName, String email, String phone) {
    User user = findByUsername(username);

    // Проверяем, не занят ли email другим пользователем
    if (!user.getEmail().equals(email) && userRepository.findByEmail(email).isPresent()) {
      throw new IllegalStateException("Email уже используется другим пользователем");
    }

    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setPhone(phone);

    userRepository.save(user);
  }

  @Override
  @Transactional
  public void changePassword(String username, String currentPassword, String newPassword, String confirmPassword) {
    User user = findByUsername(username);

    // Проверяем текущий пароль
    if (!(user.getPassword().equals(currentPassword))) {
      throw new IllegalStateException("Неверный текущий пароль");
    }

    // Проверяем совпадение паролей
    if (!newPassword.equals(confirmPassword)) {
      throw new IllegalStateException("Пароли не совпадают");
    }

    // Проверяем сложность пароля
    if (newPassword.length() < 6) {
      throw new IllegalStateException("Пароль должен содержать не менее 6 символов");
    }

    // Обновляем пароль
    user.setPassword(newPassword);
    userRepository.save(user);
  }

}