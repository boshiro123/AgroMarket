package com.AgroMarket.controller;

import com.AgroMarket.models.Cart;
import com.AgroMarket.models.CartItem;
import com.AgroMarket.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @GetMapping
  public String showCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    Cart cart = cartService.getCartForUser(userDetails.getUsername());
    model.addAttribute("cart", cart);
    model.addAttribute("totalPrice", cartService.calculateTotal(cart));
    return "cart/cart";
  }

  @PostMapping("/add/{productId}")
  @ResponseBody
  public ResponseEntity<String> addToCart(
      @PathVariable Long productId,
      @AuthenticationPrincipal UserDetails userDetails) {
    cartService.addProduct(userDetails.getUsername(), productId);
    return ResponseEntity.ok("Товар добавлен в корзину");
  }

  @PostMapping("/update/{itemId}")
  @ResponseBody
  public ResponseEntity<String> updateQuantity(
      @PathVariable Long itemId,
      @RequestParam int quantity,
      @AuthenticationPrincipal UserDetails userDetails) {
    cartService.updateQuantity(userDetails.getUsername(), itemId, quantity);
    return ResponseEntity.ok("Количество обновлено");
  }

  @PostMapping("/remove/{itemId}")
  @ResponseBody
  public ResponseEntity<String> removeFromCart(
      @PathVariable Long itemId,
      @AuthenticationPrincipal UserDetails userDetails) {
    cartService.removeItem(userDetails.getUsername(), itemId);
    return ResponseEntity.ok("Товар удален из корзины");
  }

  @PostMapping("/clear")
  @ResponseBody
  public ResponseEntity<String> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
    cartService.clearCart(userDetails.getUsername());
    return ResponseEntity.ok("Корзина очищена");
  }
}