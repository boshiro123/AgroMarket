package com.AgroMarket.service.impl;

import com.AgroMarket.models.Cart;
import com.AgroMarket.models.CartItem;
import com.AgroMarket.models.Product;
import com.AgroMarket.models.User;
import com.AgroMarket.repository.CartRepository;
import com.AgroMarket.repository.UserRepository;
import com.AgroMarket.service.CartService;
import com.AgroMarket.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final UserRepository userRepository;
  private final ProductService productService;

  @Override
  @Transactional(readOnly = true)
  public Cart getCartForUser(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    return cartRepository.findByUser(user)
        .orElseGet(() -> createCart(user));
  }

  @Override
  @Transactional
  public void addProduct(String username, Long productId) {
    Cart cart = getCartForUser(username);
    Product product = productService.findById(productId);

    Optional<CartItem> existingItem = cart.getItems().stream()
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst();

    if (existingItem.isPresent()) {
      existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
    } else {
      CartItem newItem = new CartItem();
      newItem.setCart(cart);
      newItem.setProduct(product);
      newItem.setQuantity(1);
      newItem.setPrice(product.getPrice());
      cart.getItems().add(newItem);
    }

    cartRepository.save(cart);
  }

  @Override
  @Transactional
  public void updateQuantity(String username, Long itemId, int quantity) {
    Cart cart = getCartForUser(username);
    cart.getItems().stream()
        .filter(item -> item.getId().equals(itemId))
        .findFirst()
        .ifPresent(item -> {
          if (quantity > 0) {
            item.setQuantity(quantity);
          } else {
            cart.getItems().remove(item);
          }
        });
    cartRepository.save(cart);
  }

  @Override
  @Transactional
  public void removeItem(String username, Long itemId) {
    Cart cart = getCartForUser(username);
    cart.getItems().removeIf(item -> item.getId().equals(itemId));
    cartRepository.save(cart);
  }

  @Override
  @Transactional
  public void clearCart(String username) {
    Cart cart = getCartForUser(username);
    cart.getItems().clear();
    cartRepository.save(cart);
  }

  @Override
  public BigDecimal calculateTotal(Cart cart) {
    return cart.getItems().stream()
        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private Cart createCart(User user) {
    Cart cart = new Cart();
    cart.setUser(user);
    return cartRepository.save(cart);
  }
}