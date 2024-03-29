package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.CategoryDto;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.dto.responses.HomeResponse;
import org.lashop.newback.models.ShoeType;
import org.lashop.newback.repositories.ShoeTypeRepository;
import org.lashop.newback.services.CategoryService;
import org.lashop.newback.services.ShoeTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final ShoeTypeService shoeTypeService;
    private final CategoryService categoryService;

    @GetMapping("api/home")
    public ResponseEntity<HomeResponse> getAllModels() {
//        if (principal == null) {
//            throw new IllegalArgumentException("user no authorized");
//        }

        List<ShoeTypeDto> shoeTypes = shoeTypeService.getTypesList()
                .stream().
                filter(ShoeTypeDto::isInStock)
                .toList();
        List<CategoryDto> categories = categoryService.getAllCategories();

        HomeResponse response = HomeResponse.builder()
                .shoeTypes(shoeTypes)
                .categories(categories)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
