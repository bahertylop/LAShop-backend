package org.lashop.newback.controllers;


import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.CardDto;
import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.OrderDto;
import org.lashop.newback.dto.requests.OrderCreationRequest;
import org.lashop.newback.dto.responses.InfoToOrder;
import org.lashop.newback.services.AddressService;
import org.lashop.newback.services.CardsService;
import org.lashop.newback.services.CartService;
import org.lashop.newback.services.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrdersController {

    private final CardsService cardsService;
    private final AddressService addressService;
    private final OrdersService ordersService;
    private final CartService cartService;

    @GetMapping("/api/order/info")
    public ResponseEntity<?> getInfoForOrder(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }

        AccountUserDetails userDetails = (AccountUserDetails) ((Authentication)principal).getPrincipal();

        try {
            InfoToOrder info = InfoToOrder.builder()
                    .addresses(addressService.getAllAddresses(userDetails.getId()))
                    .cards(cardsService.getUserCards(userDetails.getId()))
                    .cart(cartService.takeCart(userDetails.getId()))
                    .build();

            return ResponseEntity.ok(info);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // доделать проверки на корзину
    @PostMapping("api/order/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreationRequest request, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }

        AccountUserDetails userDetails = (AccountUserDetails) ((Authentication)principal).getPrincipal();

        if (request == null || request.getOrderDto() == null || request.getOrderDto().getCardId() == null || request.getOrderDto().getAddressId() == null || request.getOrderDto().getTotalSum() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            ordersService.makeOrder(request.getOrderDto(), request.getCart(), userDetails.getId());
            return ResponseEntity.ok("order created");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("api/adm/orders")
    public ResponseEntity<?> getAllOrders() {
        List<OrderDto> orders = ordersService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
