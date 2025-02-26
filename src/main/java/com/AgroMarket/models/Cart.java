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
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<CartItem> items = new ArrayList<>();

  @Column(nullable = false)
  private BigDecimal totalPrice;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  // Метод для добавления элемента корзины
  public void addCartItem(CartItem cartItem) {
    items.add(cartItem);
    cartItem.setCart(this);
  }

  // Метод для удаления элемента корзины
  public void removeCartItem(CartItem cartItem) {
    items.remove(cartItem);
    cartItem.setCart(null);
  }
}