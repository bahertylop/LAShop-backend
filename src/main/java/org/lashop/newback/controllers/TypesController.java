package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.services.ShoeTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yaml.snakeyaml.util.EnumUtils;

import java.text.RuleBasedCollator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TypesController {

    private final ShoeTypeService shoeTypeService;

    @GetMapping("api/adm/types/inStock")
    ResponseEntity<List<ShoeTypeDto>> getShoeTypesInStock() {
        List<ShoeTypeDto> types = shoeTypeService.getShoeTypesIsInStock(true);
        return ResponseEntity.ok().body(types);
    }

    @GetMapping("api/adm/types/notInStock")
    ResponseEntity<List<ShoeTypeDto>> getShoeTypesNotInStock() {
        List<ShoeTypeDto> types = shoeTypeService.getShoeTypesIsInStock(false);
        return ResponseEntity.ok().body(types);
    }

    @PostMapping("api/adm/types/takeNotInStock/{shoeTypeId}")
    ResponseEntity<?> takeNotInStock(@PathVariable Long shoeTypeId) {
        if (shoeTypeId == null) {
            return ResponseEntity.badRequest().body("not shoeTypeId");
        }

        try {
            shoeTypeService.changeInStock(shoeTypeId, false);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("shoe type not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body("take inStock false");
    }

    @PostMapping("api/adm/types/takeInStock/{shoeTypeId}")
    ResponseEntity<?> takeInStock(@PathVariable Long shoeTypeId) {
        if (shoeTypeId == null) {
            return ResponseEntity.badRequest().body("not shoeTypeId");
        }

        try {
            shoeTypeService.changeInStock(shoeTypeId, true);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("shoe type not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body("take inStock true");
    }



    @PostMapping("api/adm/types/add")
    ResponseEntity<?> addNewShoeType(@RequestBody ShoeTypeDto shoeTypeDto) {
        if (shoeTypeDto == null || shoeTypeDto.getBrand() == null || shoeTypeDto.getModel() == null ||
                shoeTypeDto.getColor() == null || shoeTypeDto.getPrice() == 0 || shoeTypeDto.getDescription() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            shoeTypeService.createNewShoeType(shoeTypeDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("new shoe type added");
    }
}
