package com.AgroMarket.service;

import com.AgroMarket.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
  Page<Product> findAll(Pageable pageable);

  Page<Product> findByCategory(Long categoryId, Pageable pageable);

  Product findById(Long id);

  Product save(Product product);
}