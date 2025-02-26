package com.AgroMarket.service;

import com.AgroMarket.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
  Page<Product> findAll(Pageable pageable);

  Page<Product> findByCategory(Long categoryId, Pageable pageable);

  Product findById(Long id);

  Product save(Product product);

  List<Product> findRelatedProducts(Long categoryId, Long excludeProductId, int limit);

  Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

  long countProducts();

  void delete(Long id);
}