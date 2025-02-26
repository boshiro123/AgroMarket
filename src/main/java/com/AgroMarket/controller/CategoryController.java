package com.AgroMarket.controller;

import com.AgroMarket.models.Category;
import com.AgroMarket.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public String showCategories(Model model) {
    model.addAttribute("categories", categoryService.findAll());
    model.addAttribute("newCategory", new Category());
    return "categories/list";
  }

  @PostMapping
  public String createCategory(@ModelAttribute Category category) {
    categoryService.save(category);
    return "redirect:/categories";
  }

  @PostMapping("/{id}/delete")
  public String deleteCategory(@PathVariable Long id) {
    categoryService.delete(id);
    return "redirect:/categories";
  }

  @GetMapping("/{id}/edit")
  public String editCategoryForm(@PathVariable Long id, Model model) {
    model.addAttribute("category", categoryService.findById(id));
    return "categories/edit";
  }

  @PostMapping("/{id}/edit")
  public String updateCategory(@PathVariable Long id, @ModelAttribute Category category) {
    category.setId(id);
    categoryService.save(category);
    return "redirect:/categories";
  }
}