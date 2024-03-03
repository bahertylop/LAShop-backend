package org.lashop.newback.controllers;

import lombok.RequiredArgsConstructor;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.responses.ProfileResponse;
import org.lashop.newback.services.AccountService;
import org.lashop.newback.services.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final AccountService accountService;
    private final OrdersService ordersService;

    @GetMapping("api/profile")
    public ResponseEntity<?> getUserInfo(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }

        AccountUserDetails userDetails = (AccountUserDetails) ((Authentication)principal).getPrincipal();

        try {
            ProfileResponse response = ProfileResponse.builder()
                    .accountDto(accountService.getAccount(userDetails.getId()))
                    .orders(ordersService.getUserOrders(userDetails.getId()))
                    .build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("account not found");
        }
    }
}
