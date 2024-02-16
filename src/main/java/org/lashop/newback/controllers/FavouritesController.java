package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.dto.requests.OneFavouriteRequest;
import org.lashop.newback.dto.requests.FavouritesRequest;
import org.lashop.newback.services.FavouritesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FavouritesController {

    private final FavouritesService favouritesService;

    @GetMapping("/api/favourites")
    public ResponseEntity<?> getUserFavourites(@RequestBody FavouritesRequest favouritesRequest) {
        if (favouritesRequest == null || favouritesRequest.getAccountId() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        List<ShoeTypeDto> typesList = favouritesService.getFavouritesList(favouritesRequest.getAccountId());

        return new ResponseEntity<>(typesList, HttpStatus.OK);
    }

    @PostMapping("/api/favourites/deleteAll")
    public ResponseEntity<?> deleteAllFavourites(@RequestBody(required = false) FavouritesRequest favouritesRequest) {
        if (favouritesRequest == null || favouritesRequest.getAccountId() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        favouritesService.deleteFavourites(favouritesRequest.getAccountId());
        return ResponseEntity.ok("favourites deleted");
    }

    @PostMapping("/api/favourites/add")
    public ResponseEntity<?> addToFavourites(@RequestBody OneFavouriteRequest addFavouriteRequest) {
        if (addFavouriteRequest == null || addFavouriteRequest.getAccountId() == null || addFavouriteRequest.getShoeTypeId() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            favouritesService.addShoeTypeToFavourites(addFavouriteRequest.getAccountId(), addFavouriteRequest.getShoeTypeId());
        } catch (RuntimeException e) {
            return new ResponseEntity<>("account or shoeType not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("favourite added");
    }

    @PostMapping("/api/favourites/delete")
    public ResponseEntity<?> deleteOneFavourite(@RequestBody OneFavouriteRequest deleteFavourite) {
        if (deleteFavourite == null || deleteFavourite.getAccountId() == null || deleteFavourite.getShoeTypeId() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        favouritesService.deleteFromFavourites(deleteFavourite.getAccountId(), deleteFavourite.getShoeTypeId());
        return ResponseEntity.ok("favourite deleted");
    }

}
