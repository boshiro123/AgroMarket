package com.AgroMarket.repository;

import com.AgroMarket.models.Cart;
import com.AgroMarket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findByUser(User user);
}