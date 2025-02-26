package com.AgroMarket.repository;

import com.AgroMarket.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

  @Query(value = "SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.id != :excludeProductId ORDER BY RANDOM() LIMIT :limit")
  List<Product> findRelatedProducts(
      @Param("categoryId") Long categoryId,
      @Param("excludeProductId") Long excludeProductId,
      @Param("limit") int limit);
}