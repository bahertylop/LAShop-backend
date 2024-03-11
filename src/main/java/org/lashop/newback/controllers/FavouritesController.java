package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.dto.requests.OneFavouriteRequest;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.FavouritesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;


// возможно тут нужна проверка на null при касте к AccountUserDetails (юзается везде)
@RestController
@RequiredArgsConstructor
public class FavouritesController {

    private final FavouritesService favouritesService;

    @GetMapping("/api/favourites")
    public ResponseEntity<?> getUserFavourites(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }
        AccountUserDetails accountUserDetails = (AccountUserDetails) ((Authentication) principal).getPrincipal();
        // возможно тут нужна проверка на null
        List<ShoeTypeDto> typesList = favouritesService.getFavouritesList(accountUserDetails.getId());

        return new ResponseEntity<>(typesList, HttpStatus.OK);
    }

    @PostMapping("/api/favourites/deleteAll")
    public ResponseEntity<?> deleteAllFavourites(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }
        AccountUserDetails accountUserDetails = (AccountUserDetails) ((Authentication) principal).getPrincipal();

        favouritesService.deleteFavourites(accountUserDetails.getId());
        return ResponseEntity.ok("favourites deleted");
    }

    @PostMapping("/api/favourites/add")
    public ResponseEntity<?> addToFavourites(@RequestBody OneFavouriteRequest addFavouriteRequest, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }
        AccountUserDetails accountUserDetails = (AccountUserDetails) ((Authentication) principal).getPrincipal();

        if (addFavouriteRequest == null || addFavouriteRequest.getShoeTypeId() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            favouritesService.addShoeTypeToFavourites(accountUserDetails.getId(), addFavouriteRequest.getShoeTypeId());
        } catch (RuntimeException e) {
            return new ResponseEntity<>("account or shoeType not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("favourite added");
    }

    @PostMapping("/api/favourites/delete")
    public ResponseEntity<?> deleteOneFavourite(@RequestBody OneFavouriteRequest deleteFavourite, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("user no authorized");
        }
        AccountUserDetails accountUserDetails = (AccountUserDetails) ((Authentication) principal).getPrincipal();

        if (deleteFavourite == null || deleteFavourite.getShoeTypeId() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        favouritesService.deleteFromFavourites(accountUserDetails.getId(), deleteFavourite.getShoeTypeId());
        return ResponseEntity.ok("favourite deleted");
    }

}
