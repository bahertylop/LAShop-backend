package org.lashop.newback.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.*;
import org.lashop.newback.repositories.AccountRepository;
import org.lashop.newback.repositories.FavouritesRepository;
import org.lashop.newback.repositories.ShoeTypeRepository;
import org.lashop.newback.services.FavouritesService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;


public class FavouritesServiceImplTest {

    @Mock
    private FavouritesRepository favouritesRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ShoeTypeRepository shoeTypeRepository;

    @InjectMocks
    private FavouritesServiceImpl favouritesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetFavouritesList() {
        long accountId = 1L;
        List<Favourite> favourites = List.of(
                Favourite.builder()
                        .id(1)
                        .account(Account.builder().id(accountId).build())
                        .shoeType(ShoeType.builder().id(1L)
                                .category(Category.builder().id(1).build())
                                .brand("adidas")
                                .model("superstar")
                                .color("white")
                                .photos(List.of("image"))
                                .description("desc")
                                .price(100)
                                .inStock(true)
                                .products(List.of(Product.builder().id(2).build()))
                        .build()).build(),
                Favourite.builder()
                        .id(2)
                        .account(Account.builder().id(accountId).build())
                        .shoeType(ShoeType.builder().id(2L)
                            .category(Category.builder().id(1).build())
                            .brand("adidas")
                            .model("superstar")
                            .color("white")
                            .photos(List.of("image"))
                            .description("desc")
                            .price(100)
                            .inStock(true)
                            .products(List.of(Product.builder().id(2).build()))
                        .build()).build()
        );
        when(favouritesRepository.findAllByAccountId(accountId)).thenReturn(favourites);

        List<ShoeTypeDto> result = favouritesService.getFavouritesList(accountId);

        verify(favouritesRepository, times(1)).findAllByAccountId(accountId);
        Assertions.assertEquals(favourites.size(), result.size());
        Assertions.assertEquals(favourites.get(0).getShoeType().getId(), result.get(0).getId());
        Assertions.assertEquals(ShoeTypeDto.from(favourites.stream().map(Favourite::getShoeType).toList()), result);
    }

    @Test
    void testDeleteFavourites() {
        long accountId = 1L;

        favouritesService.deleteFavourites(accountId);

        verify(favouritesRepository, times(1)).deleteAllByAccountId(accountId);
    }

    @Test
    void testAddShoeTypeToFavourites() {
        long accountId = 1L;
        long shoeTypeId = 1L;
        when(favouritesRepository.findByAccountIdAndShoeTypeId(accountId, shoeTypeId)).thenReturn(Optional.empty());
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(Account.builder().id(accountId).build()));
        when(shoeTypeRepository.findById(shoeTypeId)).thenReturn(Optional.of(ShoeType.builder().id(shoeTypeId).build()));

        favouritesService.addShoeTypeToFavourites(accountId, shoeTypeId);

        verify(favouritesRepository, times(1)).findByAccountIdAndShoeTypeId(accountId, shoeTypeId);
        verify(accountRepository, times(1)).findById(accountId);
        verify(shoeTypeRepository, times(1)).findById(shoeTypeId);
        verify(favouritesRepository, times(1)).save(any(Favourite.class));
    }

    @Test
    void testAddShoeTypeToFavouritesThrowsException() {
        long accountId = 1L;
        long shoeTypeId = 1L;

        when(favouritesRepository.findByAccountIdAndShoeTypeId(accountId, shoeTypeId)).thenReturn(Optional.empty());
        when(accountRepository.findById(accountId)).thenThrow(RuntimeException.class);
        when(shoeTypeRepository.findById(shoeTypeId)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> {
            favouritesService.addShoeTypeToFavourites(accountId, shoeTypeId);
        });
    }

    @Test
    void testDeleteFromFavourites() {
        long accountId = 1L;
        long productId = 1L;

        favouritesService.deleteFromFavourites(accountId, productId);

        verify(favouritesRepository, times(1)).deleteByAccountIdAndShoeTypeId(accountId, productId);
    }
}