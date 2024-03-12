package org.lashop.newback.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lashop.newback.models.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartDtoTest {

    @Test
    void testFromForOneObject() {
        ShoeTypeDto shoeTypeDto = ShoeTypeDto.builder()
                .id(1)
                .brand("Nike")
                .model("Air Max")
                .color("Black")
                .categoryId(1)
                .productsId(List.of(1L))
                .build();

        CartDto cartDtoRes = CartDto.builder()
                .id(1)
                .shoeType(shoeTypeDto)
                .size(10.5)
                .quantity(2)
                .accountId(1)
                .build();

        Cart cart = Cart.builder()
                .id(1)
                .shoeType(ShoeType.builder()
                        .id(1)
                        .brand("Nike")
                        .model("Air Max")
                        .color("Black")
                        .category(Category.builder().id(1).build())
                        .products(List.of(Product.builder().id(1).build()))
                        .build())
                .size(10.5)
                .quantity(2)
                .account(Account.builder().id(1).build())
                .build();

        CartDto cartDtoMy = CartDto.from(cart);
        Assertions.assertEquals(cartDtoRes, cartDtoMy);
    }

    @Test
    void testFromForListOfObjects() {
        ShoeTypeDto shoeTypeDto1 = ShoeTypeDto.builder()
                .id(1)
                .brand("Nike")
                .model("Air Max")
                .color("Black")
                .categoryId(1)
                .productsId(List.of(1L))
                .build();

        ShoeTypeDto shoeTypeDto2 = ShoeTypeDto.builder()
                .id(2)
                .brand("Adidas")
                .model("Superstar")
                .color("White")
                .categoryId(2)
                .productsId(List.of(2L))
                .build();

        List<Cart> cartList = List.of(
                Cart.builder()
                        .id(1)
                        .shoeType(ShoeType.builder().id(1).build())
                        .size(10.5)
                        .quantity(2)
                        .account(Account.builder().id(1).build())
                        .shoeType(ShoeType.builder()
                                .id(1)
                                .brand("Nike")
                                .model("Air Max")
                                .color("Black")
                                .category(Category.builder().id(1).build())
                                .products(List.of(Product.builder().id(1).build()))
                                .build())
                        .build(),
                Cart.builder()
                        .id(2)
                        .shoeType(ShoeType.builder().id(2).build())
                        .size(9.5)
                        .quantity(1)
                        .account(Account.builder().id(2).build())
                        .shoeType(ShoeType.builder()
                                .id(2)
                                .brand("Adidas")
                                .model("Superstar")
                                .color("White")
                                .category(Category.builder().id(2).build())
                                .products(List.of(Product.builder().id(2).build()))
                                .build())
                        .build()
        );

        List<CartDto> cartDtoList = CartDto.from(cartList);

        List<CartDto> expectedCartDtoList = List.of(
                CartDto.builder()
                        .id(1)
                        .shoeType(shoeTypeDto1)
                        .size(10.5)
                        .quantity(2)
                        .accountId(1)

                        .build(),
                CartDto.builder()
                        .id(2)
                        .shoeType(shoeTypeDto2)
                        .size(9.5)
                        .quantity(1)
                        .accountId(2)
                        .build()
        );

        Assertions.assertEquals(expectedCartDtoList, cartDtoList);
    }
}