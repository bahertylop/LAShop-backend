package org.lashop.newback.controllers;

import lombok.RequiredArgsConstructor;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.CardDto;
import org.lashop.newback.services.CardsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CardController {

    private final CardsService cardsService;

    @GetMapping("api/cards/get")
    public ResponseEntity<?> getUserCards(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }

        AccountUserDetails userDetails = (AccountUserDetails) ((Authentication)principal).getPrincipal();

        try {
            List<CardDto> cards = cardsService.getUserCards(userDetails.getId());
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("api/cards/add")
    public ResponseEntity<?> addCard(@RequestBody CardDto cardDto, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }

        AccountUserDetails userDetails = (AccountUserDetails) ((Authentication)principal).getPrincipal();

        if (cardDto == null || cardDto.getCardCVV() == null || cardDto.getCardDate() == null ||
                cardDto.getCardNumber() == null || cardDto.getPaySystem() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            cardsService.addNewCard(userDetails.getId(), cardDto);
            return ResponseEntity.ok("card added");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
