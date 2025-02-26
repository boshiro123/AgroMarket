package com.AgroMarket.service.impl;

import com.AgroMarket.models.*;
import com.AgroMarket.repository.OrderRepository;
import com.AgroMarket.service.CartService;
import com.AgroMarket.service.OrderService;
import com.AgroMarket.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final CartService cartService;
  private final UserService userService;

  @Override
  @Transactional
  public Order createOrderFromCart(String username, String shippingAddress, String paymentMethod) {
    // Получаем корзину пользователя
    Cart cart = cartService.getCartForUser(username);
    if (cart.getItems().isEmpty()) {
      throw new IllegalStateException("Невозможно создать заказ: корзина пуста");
    }

    // Создаем новый заказ
    final Order order = new Order(cart.getUser(), "PENDING", shippingAddress, paymentMethod,
        cartService.calculateTotal(cart));

    // Переносим товары из корзины в заказ
    cart.getItems().forEach(cartItem -> {
      OrderItem orderItem = OrderItem.builder()
          .order(order)
          .product(cartItem.getProduct())
          .quantity(cartItem.getQuantity())
          .price(cartItem.getPrice())
          .build();
      order.addOrderItem(orderItem);
    });

    // Сохраняем заказ
    Order newOrder = orderRepository.save(order);

    // Очищаем корзину
    cartService.clearCart(username);

    return newOrder;
  }

  @Override
  @Transactional(readOnly = true)
  public Order findById(Long id) {
    return orderRepository.findByIdWithItems(id)
        .orElseThrow(() -> new EntityNotFoundException("Заказ с ID " + id + " не найден"));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Order> findByUsername(String username, Pageable pageable) {
    return orderRepository.findByUserUsernameOrderByCreatedAtDesc(username, pageable);
  }

  @Override
  @Transactional
  public void updateOrderStatus(Long orderId, String status) {
    Order order = findById(orderId);
    order.setStatus(status);
    orderRepository.save(order);
  }
}