package com.AgroMarket.service.impl;

import com.AgroMarket.models.Category;
import com.AgroMarket.repository.CategoryRepository;
import com.AgroMarket.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  @Cacheable("categories")
  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Category findById(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Категория не найдена: " + id));
  }

  @Override
  @Transactional
  @CacheEvict(value = "categories", allEntries = true)
  public Category save(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  @Transactional
  @CacheEvict(value = "categories", allEntries = true)
  public void delete(Long id) {
    categoryRepository.deleteById(id);
  }
}