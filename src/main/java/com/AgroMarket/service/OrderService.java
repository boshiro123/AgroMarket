package com.AgroMarket.service;

import com.AgroMarket.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

public interface OrderService {
  Order createOrderFromCart(String username, String shippingAddress, String paymentMethod);

  Order findById(Long id);

  Page<Order> findByUsername(String username, Pageable pageable);

  void updateOrderStatus(Long orderId, String status);

  Page<Order> findAll(Pageable pageable);

  long countOrders();

  BigDecimal calculateTotalRevenue();
}