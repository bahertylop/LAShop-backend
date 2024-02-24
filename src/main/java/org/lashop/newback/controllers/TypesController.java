package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.services.ShoeTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
