package com.AgroMarket.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  @Column(nullable = false)
  private BigDecimal totalPrice;

  @Column(nullable = false)
  private String status; // PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED

  private String shippingAddress;
  private String paymentMethod;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Order(User user, String status, String shippingAddress, String paymentMethod, BigDecimal totalPrice) {
    this.user = user;
    this.status = status;
    this.shippingAddress = shippingAddress;
    this.paymentMethod = paymentMethod;
    this.totalPrice = totalPrice;
    this.items = new ArrayList<>();
  }

  // Метод для добавления элемента заказа
  public void addOrderItem(OrderItem orderItem) {
    if (items == null) {
      items = new ArrayList<>();
    }
    items.add(orderItem);
    orderItem.setOrder(this);
  }

  // Метод для удаления элемента заказа
  public void removeOrderItem(OrderItem orderItem) {
    if (items != null) {
      items.remove(orderItem);
      orderItem.setOrder(null);
    }
  }
}