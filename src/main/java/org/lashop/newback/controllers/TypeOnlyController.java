package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.services.ProductService;
import org.lashop.newback.services.ShoeTypeService;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TypeOnlyController {

    private final ShoeTypeService shoeTypeService;
    private final ProductService productService;
}
