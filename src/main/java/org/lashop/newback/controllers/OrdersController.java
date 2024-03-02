package org.lashop.newback.controllers;


import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.responses.InfoToOrder;
import org.lashop.newback.services.AddressService;
import org.lashop.newback.services.CardsService;
import org.lashop.newback.services.CartService;
import org.lashop.newback.services.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class OrdersController {

    private final CardsService cardsService;
    private final AddressService addressService;
    private final OrdersService ordersService;
    private final CartService cartService;

    @GetMapping("/api/order/create")
    public ResponseEntity<?> getInfoForOrder(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }

        AccountUserDetails userDetails = (AccountUserDetails) ((Authentication)principal).getPrincipal();

        InfoToOrder info = InfoToOrder.builder()
                .addresses(addressService.getAllAddresses(userDetails.getId()))
                .cards(cardsService.getAllCards(userDetails.getId()))
                .cart(cartService.takeCart(userDetails.getId()))
                .build();

        return ResponseEntity.ok(info);
    }



}
