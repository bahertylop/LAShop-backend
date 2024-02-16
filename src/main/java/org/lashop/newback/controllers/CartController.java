package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.requests.AccountIdRequest;
import org.lashop.newback.dto.requests.CurrentShoe;
import org.lashop.newback.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/api/cart")
    public ResponseEntity<?> getUserCart(@RequestBody AccountIdRequest accountIdRequest) {
        if (accountIdRequest == null || accountIdRequest.getAccountId() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        List<CartDto> cartDto = cartService.takeCart(accountIdRequest.getAccountId());

        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    // можно подредачить чтобы возвращал исключения если добавляются кросы которых нет

    @PostMapping("/api/cart/minus")
    public ResponseEntity<?> minusQuantity(@RequestBody CurrentShoe currentShoe) {
        if (currentShoe == null || currentShoe.getShoeTypeId() == null
                || currentShoe.getSize() == null || currentShoe.getAccountId() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        cartService.minusCount(currentShoe.getShoeTypeId(), currentShoe.getSize(), currentShoe.getAccountId());
        return ResponseEntity.ok("minused");
    }

    @PostMapping("/api/cart/plus")
    public ResponseEntity<?> plusQuantity(@RequestBody CurrentShoe currentShoe) {
        if (currentShoe == null || currentShoe.getShoeTypeId() == null
                || currentShoe.getSize() == null || currentShoe.getAccountId() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        cartService.plusCount(currentShoe.getShoeTypeId(), currentShoe.getSize(), currentShoe.getAccountId());
        return ResponseEntity.ok("plused");
    }

    @PostMapping("/api/cart/delete")
    public ResponseEntity<?> deletePositionFromCart(@RequestBody CurrentShoe currentShoe) {
        if (currentShoe == null || currentShoe.getShoeTypeId() == null
                || currentShoe.getSize() == null || currentShoe.getAccountId() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        cartService.deleteItemFromCart(currentShoe.getShoeTypeId(), currentShoe.getSize(), currentShoe.getAccountId());
        return ResponseEntity.ok("deleted");
    }

    @PostMapping("/api/cart/add")
    public ResponseEntity<?> addToCart(@RequestBody CurrentShoe currentShoe) {
        if (currentShoe == null || currentShoe.getShoeTypeId() == null
                || currentShoe.getSize() == null || currentShoe.getAccountId() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            cartService.addToCart(currentShoe.getShoeTypeId(), currentShoe.getSize(), currentShoe.getAccountId());
        } catch (RuntimeException e) {
            return new ResponseEntity<>("account or shoeType not found, or not in stock", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("added");
    }
}
