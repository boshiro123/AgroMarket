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

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<OrderItem> orderItems = new ArrayList<>();

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

  // Метод для добавления элемента заказа
  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  // Метод для удаления элемента заказа
  public void removeOrderItem(OrderItem orderItem) {
    orderItems.remove(orderItem);
    orderItem.setOrder(null);
  }
}