package com.AgroMarket.controller;

import com.AgroMarket.models.Order;
import com.AgroMarket.models.User;
import com.AgroMarket.models.Product;
import com.AgroMarket.service.OrderService;
import com.AgroMarket.service.UserService;
import com.AgroMarket.service.ProductService;
import com.AgroMarket.service.CategoryService;
import com.AgroMarket.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final UserService userService;
  private final OrderService orderService;
  private final ProductService productService;
  private final CategoryService categoryService;
  private final FileStorageService fileStorageService;

  @GetMapping
  public String dashboard(Model model) {
    // Получаем статистику для дашборда
    long totalUsers = userService.countUsers();
    long totalOrders = orderService.countOrders();
    long totalProducts = productService.countProducts();
    BigDecimal totalRevenue = orderService.calculateTotalRevenue();

    model.addAttribute("totalUsers", totalUsers);
    model.addAttribute("totalOrders", totalOrders);
    model.addAttribute("totalProducts", totalProducts);
    model.addAttribute("totalRevenue", totalRevenue);
    model.addAttribute("currentPath", "/admin");

    // Последние заказы
    Page<Order> recentOrders = orderService.findAll(
        PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")));
    model.addAttribute("recentOrders", recentOrders.getContent());

    return "admin/dashboard";
  }

  @GetMapping("/orders")
  public String orders(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      Model model) {
    Page<Order> orders = orderService.findAll(
        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    model.addAttribute("orders", orders.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", orders.getTotalPages());
    model.addAttribute("currentPath", "/admin/orders");
    return "admin/orders";
  }

  @GetMapping("/users")
  public String users(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      Model model) {
    Page<User> users = userService.findAll(
        PageRequest.of(page, size, Sort.by("username")));
    model.addAttribute("users", users.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", users.getTotalPages());
    model.addAttribute("currentPath", "/admin/users");
    return "admin/users";
  }

  @PostMapping("/orders/{id}/status")
  @ResponseBody
  public ResponseEntity<String> updateOrderStatus(
      @PathVariable Long id,
      @RequestParam String status) {
    try {
      orderService.updateOrderStatus(id, status);
      return ResponseEntity.ok("Статус заказа успешно обновлен");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/products")
  public String products(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      Model model) {
    Page<Product> products = productService.findAll(
        PageRequest.of(page, size, Sort.by("name")));

    model.addAttribute("products", products.getContent());
    model.addAttribute("categories", categoryService.findAll());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", products.getTotalPages());
    model.addAttribute("currentPath", "/admin/products");

    return "admin/products";
  }

  @PostMapping("/products")
  public String createProduct(
      @RequestParam String name,
      @RequestParam String description,
      @RequestParam BigDecimal price,
      @RequestParam int quantity,
      @RequestParam Long categoryId,
      @RequestParam(required = false) MultipartFile image,
      RedirectAttributes redirectAttributes) {
    try {
      Product product = new Product();
      product.setName(name);
      product.setDescription(description);
      product.setPrice(price);
      product.setQuantity(quantity);
      product.setCategory(categoryService.findById(categoryId));

      if (image != null && !image.isEmpty()) {
        String imageUrl = fileStorageService.saveFile(image);
        product.setImageUrl(imageUrl);
      }

      productService.save(product);
      redirectAttributes.addFlashAttribute("successMessage", "Товар успешно добавлен");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении товара: " + e.getMessage());
    }
    return "redirect:/admin/products";
  }

  @DeleteMapping("/products/{id}")
  @ResponseBody
  public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
    try {
      Product product = productService.findById(id);
      if (product.getImageUrl() != null) {
        fileStorageService.deleteFile(product.getImageUrl());
      }
      productService.delete(id);
      return ResponseEntity.ok("Товар успешно удален");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/orders/{id}")
  public String orderDetails(@PathVariable Long id, Model model) {
    Order order = orderService.findById(id);
    model.addAttribute("order", order);
    model.addAttribute("currentPath", "/admin/orders");
    return "admin/order-details";
  }
}