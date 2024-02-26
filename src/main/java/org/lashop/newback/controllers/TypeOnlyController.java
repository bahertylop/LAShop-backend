package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.dto.requests.SizeQuantity;
import org.lashop.newback.dto.responses.TypeOnlyResponse;
import org.lashop.newback.dto.responses.TypeOnlyResponseAdm;
import org.lashop.newback.services.CategoryService;
import org.lashop.newback.services.ProductService;
import org.lashop.newback.services.ShoeTypeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.channels.ReadPendingException;
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
    ResponseEntity<?> getShoeTypeCard(@PathVariable Long shoeTypeId) {
        if (shoeTypeId == null) {
            return ResponseEntity.badRequest().body("not shoeTypeId");
        }

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

    @GetMapping("/api/adm/products/{shoeTypeId}")
    ResponseEntity<?> getShoeTypeCardAdm(@PathVariable Long shoeTypeId) {
        if (shoeTypeId == null) {
            return ResponseEntity.badRequest().body("not shoeTypeId");
        }

        try {
            ShoeTypeDto shoeType = shoeTypeService.getTypeById(shoeTypeId);
            List<Double> sizes = productService.getAllSizesForType(shoeType.getId());

            TypeOnlyResponseAdm resp = TypeOnlyResponseAdm.builder()
                    .shoeTypeDto(shoeType)
                    .sizes(sizes)
                    .build();

            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/api/adm/products/{shoeTypeId}/update")
    ResponseEntity<?> updateShoeType(@RequestBody ShoeTypeDto shoeTypeDto, @PathVariable Long shoeTypeId) {
        if (shoeTypeId == null) {
            return ResponseEntity.badRequest().body("not shoeTypeId");
        }

        if (shoeTypeDto == null || shoeTypeDto.getBrand() == null || shoeTypeDto.getModel() == null ||
        shoeTypeDto.getColor() == null || shoeTypeDto.getDescription() == null || shoeTypeDto.getPrice() <= 0) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            shoeTypeDto.setId(shoeTypeId);
            shoeTypeService.updateShoeType(shoeTypeDto);
            return ResponseEntity.ok("shoe type updated");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/adm/products/{shoeTypeId}/add")
    public ResponseEntity<?> addShoePairs(@RequestBody SizeQuantity sizeQuantity, @PathVariable Long shoeTypeId) {
        if (shoeTypeId == null) {
            return ResponseEntity.badRequest().body("not shoeTypeId");
        }

        if (sizeQuantity.getQuantity() == null || sizeQuantity.getSize() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            productService.addSomeNewProducts(shoeTypeId, sizeQuantity.getSize(), sizeQuantity.getQuantity());
            return ResponseEntity.ok("pairs added");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
