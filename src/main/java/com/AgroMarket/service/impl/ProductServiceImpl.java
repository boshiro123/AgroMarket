package com.AgroMarket.service.impl;

import com.AgroMarket.models.Product;
import com.AgroMarket.repository.ProductRepository;
import com.AgroMarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Product> findByCategory(Long categoryId, Pageable pageable) {
    return productRepository.findByCategoryId(categoryId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Product findById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Продукт не найден: " + id));
  }

  @Override
  @Transactional
  public Product save(Product product) {
    return productRepository.save(product);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Product> findRelatedProducts(Long categoryId, Long excludeProductId, int limit) {
    return productRepository.findRelatedProducts(categoryId, excludeProductId, limit);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
    return productRepository.findByCategoryId(categoryId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public long countProducts() {
    return productRepository.count();
  }

  @Override
  @Transactional
  public void delete(Long id) {
    productRepository.deleteById(id);
  }
}