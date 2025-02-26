package com.AgroMarket.service.impl;

import com.AgroMarket.models.Product;
import com.AgroMarket.repository.ProductRepository;
import com.AgroMarket.service.ProductService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Override
  public Page<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable);
  }

  @Override
  public Page<Product> findByCategory(Long categoryId, Pageable pageable) {
    return productRepository.findByCategoryId(categoryId, pageable);
  }

  @Override
  public Product findById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Продукт с id " + id + " не найден"));
  }

  @Override
  public Product save(Product product) {
    return productRepository.save(product);
  }
}