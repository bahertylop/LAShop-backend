package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.dto.responses.TypeOnlyResponse;
import org.lashop.newback.services.CategoryService;
import org.lashop.newback.services.ProductService;
import org.lashop.newback.services.ShoeTypeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TypeOnlyController {

    private final ShoeTypeService shoeTypeService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Value("${relatedProductsCount}")
    private int relatedTypesLimit;

    @GetMapping("/api/products/{shoeTypeId}")
    ResponseEntity<?> getShoeTypeCard(@PathVariable(required = false) long shoeTypeId) {
        try {
            ShoeTypeDto shoeType = shoeTypeService.getTypeById(shoeTypeId);
            List<ShoeTypeDto> relatedProducts = categoryService.getCategoryShoeTypesLimited(shoeType.getCategoryId(), relatedTypesLimit);
            List<Double> sizes = productService.getAllSizesForType(shoeType.getId());

            TypeOnlyResponse resp = TypeOnlyResponse.builder()
                    .shoeTypeDto(shoeType)
                    .relatedShoeTypes(relatedProducts)
                    .sizes(sizes)
                    .build();

            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
