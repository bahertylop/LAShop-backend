package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.CategoryDto;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoryService categoryService;

    @GetMapping("api/categories/{categoryId}")
    ResponseEntity<?> getCategoryShoeTypes(@PathVariable(required = false) Long categoryId) {
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


    
}
