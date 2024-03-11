package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.AddressDto;
import org.lashop.newback.services.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("api/address/get")
    public ResponseEntity<?> getUserAddresses(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }

        AccountUserDetails userDetails = (AccountUserDetails) ((Authentication)principal).getPrincipal();

        try {
            List<AddressDto> addresses = addressService.getAllAddresses(userDetails.getId());
            return ResponseEntity.ok(addresses);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("api/address/add")
    public ResponseEntity<?> addAddress(@RequestBody String address, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }
        AccountUserDetails userDetails = (AccountUserDetails) ((Authentication)principal).getPrincipal();

        if (address == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            addressService.addNewAddress(userDetails.getId(), address);
            return ResponseEntity.ok("address added");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
