package org.lashop.newback.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lashop.newback.models.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FavouritesDtoTest {

    @Test
    void testFromForOneObject() {
        FavouritesDto favouritesDtoRes = FavouritesDto.builder()
                .id(1)
                .account(AccountDto.builder()
                        .id(2)
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .phoneNumber("987654321")
                        .password("password456")
                        .personalSale(20)
                        .role("ADMIN")
                        .accountState("NOT_CONFIRMED")
                        .addresses(List.of(3L, 4L))
                        .cards(List.of(3L, 4L))
                        .orders(List.of(3L, 4L))
                        .build())
                .product(ShoeTypeDto.builder()
                        .id(1)
                        .brand("Nike")
                        .model("Air Max")
                        .color("Black")
                        .categoryId(1)
                        .productsId(List.of(1L))
                        .build())
                .build();

        Favourite favourite = Favourite.builder()
                .id(1)
                .account(Account.builder()
                        .id(2)
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .phoneNumber("987654321")
                        .password("password456")
                        .personalSale(20)
                        .role(Account.Role.ADMIN)
                        .accountState(Account.State.NOT_CONFIRMED)
                        .cards(List.of(Card.builder().id(3).build(), Card.builder().id(4).build()))
                        .addresses(List.of(Address.builder().id(3).build(), Address.builder().id(4).build()))
                        .orders(List.of(Orders.builder().id(3).build(), Orders.builder().id(4).build()))
                        .build())
                .shoeType(ShoeType.builder()
                        .id(1)
                        .brand("Nike")
                        .model("Air Max")
                        .color("Black")
                        .category(Category.builder().id(1).build())
                        .products(List.of(Product.builder().id(1).build()))
                        .build())
                .build();

        FavouritesDto favouritesDtoMy = FavouritesDto.from(favourite);
        Assertions.assertEquals(favouritesDtoRes, favouritesDtoMy);
    }

    @Test
    void testFromForListOfObjects() {
        List<Favourite> favouriteList = List.of(
                Favourite.builder()
                        .id(1)
                        .account(Account.builder()
                                .id(2)
                                .firstName("Jane")
                                .lastName("Smith")
                                .email("jane.smith@example.com")
                                .phoneNumber("987654321")
                                .password("password456")
                                .personalSale(20)
                                .role(Account.Role.ADMIN)
                                .accountState(Account.State.NOT_CONFIRMED)
                                .cards(List.of(Card.builder().id(3).build(), Card.builder().id(4).build()))
                                .addresses(List.of(Address.builder().id(3).build(), Address.builder().id(4).build()))
                                .orders(List.of(Orders.builder().id(3).build(), Orders.builder().id(4).build()))
                                .build())
                        .shoeType(ShoeType.builder()
                                .id(1)
                                .brand("Nike")
                                .model("Air Max")
                                .color("Black")
                                .category(Category.builder().id(1).build())
                                .products(List.of(Product.builder().id(1).build()))
                                .build())
                        .build()
        );

        List<FavouritesDto> favouritesDtoList = FavouritesDto.from(favouriteList);

        List<FavouritesDto> expectedFavouritesDtoList = List.of(
                FavouritesDto.builder()
                        .id(1)
                        .account(AccountDto.builder()
                                .id(2)
                                .firstName("Jane")
                                .lastName("Smith")
                                .email("jane.smith@example.com")
                                .phoneNumber("987654321")
                                .password("password456")
                                .personalSale(20)
                                .role("ADMIN")
                                .accountState("NOT_CONFIRMED")
                                .addresses(List.of(3L, 4L))
                                .cards(List.of(3L, 4L))
                                .orders(List.of(3L, 4L))
                                .build())
                        .product(ShoeTypeDto.builder()
                                .id(1)
                                .brand("Nike")
                                .model("Air Max")
                                .color("Black")
                                .categoryId(1)
                                .productsId(List.of(1L))
                                .build())
                        .build()
        );

        Assertions.assertEquals(expectedFavouritesDtoList, favouritesDtoList);
    }
}