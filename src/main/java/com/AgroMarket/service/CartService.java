package com.AgroMarket.service;

import com.AgroMarket.models.Cart;
import java.math.BigDecimal;

public interface CartService {
  Cart getCartForUser(String username);

  void addProduct(String username, Long productId);

  void updateQuantity(String username, Long itemId, int quantity);

  void removeItem(String username, Long itemId);

  void clearCart(String username);

  BigDecimal calculateTotal(Cart cart);
}