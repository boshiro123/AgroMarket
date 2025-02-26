package com.AgroMarket.controller;

import com.AgroMarket.models.Order;
import com.AgroMarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  public String listOrders(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @AuthenticationPrincipal UserDetails userDetails,
      Model model) {
    Page<Order> orders = orderService.findByUsername(
        userDetails.getUsername(),
        PageRequest.of(page, size));
    model.addAttribute("orders", orders.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", orders.getTotalPages());
    return "orders/list";
  }

  @GetMapping("/{id}")
  public String showOrder(@PathVariable Long id, Model model) {
    try {
      Order order = orderService.findById(id);
      model.addAttribute("order", order);
      return "orders/details";
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Заказ не найден", e);
    }
  }

  @GetMapping("/create")
  public String showOrderForm(
      @AuthenticationPrincipal UserDetails userDetails,
      Model model) {
    return "orders/create";
  }

  @PostMapping("/create")
  public String createOrder(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestParam String shippingAddress,
      @RequestParam String paymentMethod,
      RedirectAttributes redirectAttributes) {
    try {
      Order order = orderService.createOrderFromCart(
          userDetails.getUsername(),
          shippingAddress,
          paymentMethod);
      redirectAttributes.addFlashAttribute(
          "successMessage",
          "Заказ успешно создан. Номер заказа: " + order.getId());
      return "redirect:/orders/" + order.getId();
    } catch (IllegalStateException e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return "redirect:/cart";
    }
  }
}