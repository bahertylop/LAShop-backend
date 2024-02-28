package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.services.AddressService;
import org.lashop.newback.services.CardsService;
import org.lashop.newback.services.OrdersService;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrdersController {

    private final CardsService cardsService;
    private final AddressService addressService;
    private final OrdersService ordersService;


}
