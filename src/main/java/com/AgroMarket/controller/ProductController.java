package com.AgroMarket.controller;

import com.AgroMarket.models.Product;
import com.AgroMarket.service.CategoryService;
import com.AgroMarket.service.FileStorageService;
import com.AgroMarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;
  private final CategoryService categoryService;
  private final FileStorageService fileStorageService;

  @GetMapping
  public String showProducts(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "9") int size,
      @RequestParam(required = false) Long categoryId,
      Model model) {

    Page<Product> productPage;
    if (categoryId != null) {
      productPage = productService.findByCategory(categoryId, PageRequest.of(page, size));
    } else {
      productPage = productService.findAll(PageRequest.of(page, size));
    }

    model.addAttribute("products", productPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", productPage.getTotalPages());
    model.addAttribute("categoryId", categoryId);
    model.addAttribute("categories", categoryService.findAll());

    return "products/catalog";
  }

  @GetMapping("/{id}")
  public String showProductDetails(@PathVariable Long id, Model model) {
    Product product = productService.findById(id);
    model.addAttribute("product", product);
    model.addAttribute("category", product.getCategory());
    model.addAttribute("relatedProducts",
        productService.findRelatedProducts(product.getCategory().getId(), id, 3));
    return "products/details";
  }

  @PostMapping("/{id}/image")
  public String uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) throws IOException {
    Product product = productService.findById(id);

    // Удаляем старое изображение, если оно есть
    if (product.getImageUrl() != null) {
      fileStorageService.deleteFile(product.getImageUrl());
    }

    // Сохраняем новое изображение
    String imageUrl = fileStorageService.saveFile(file);
    product.setImageUrl(imageUrl);
    productService.save(product);

    return "redirect:/products/" + id;
  }
}