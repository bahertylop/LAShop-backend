package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.CategoryDto;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoryService categoryService;

    @GetMapping("api/categories/{categoryId}")
    public ResponseEntity<?> getCategoryShoeTypes(@PathVariable(required = false) Long categoryId) {
//        if (categoryId == null) {
//            return new ResponseEntity<>("not valid path", HttpStatus.BAD_REQUEST);
//        }

        try {
            CategoryDto category = categoryService.getCategoryById(categoryId);
            List<ShoeTypeDto> shoeTypeDtoList = categoryService.getCategoryShoeTypes(category.getId())
                    .stream()
                    .filter(ShoeTypeDto::isInStock)
                    .toList();
            return ResponseEntity.ok().body(shoeTypeDtoList);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("category not found", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("api/adm/categories/all")
    public ResponseEntity<?> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("api/adm/categories/add")
    public ResponseEntity<?> addNewCategory(@RequestBody CategoryDto categoryDto) {
        if (categoryDto == null || categoryDto.getName() == null || categoryDto.getImage() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            categoryService.createCategory(categoryDto.getName(), categoryDto.getImage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("category added");
    }

    @PostMapping("api/adm/categories/delete")
    public ResponseEntity<?> deleteCategory(@RequestBody CategoryDto categoryDto) {
        if (categoryDto == null || categoryDto.getName() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        categoryService.deleteCategoryAndTakeTypesNotInStock(categoryDto.getName());
        return ResponseEntity.ok("category deleted");
    }
}
