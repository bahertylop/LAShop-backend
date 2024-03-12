package org.lashop.newback.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lashop.newback.models.Category;
import org.lashop.newback.models.Product;
import org.lashop.newback.models.ShoeType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShoeTypeDtoTest {

    @Test
    void testFromForOneObject() {
        ShoeTypeDto shoeTypeDtoRes = ShoeTypeDto.builder()
                .id(1L)
                .brand("Nike")
                .model("Air Max")
                .categoryId(1L)
                .color("Black")
                .photos(List.of("photo1.jpg", "photo2.jpg"))
                .description("Comfortable running shoes")
                .price(100)
                .inStock(true)
                .productsId(List.of(1L, 2L, 3L))
                .build();

        ShoeType shoeType = ShoeType.builder()
                .id(1L)
                .brand("Nike")
                .model("Air Max")
                .category(Category.builder().id(1L).build())
                .color("Black")
                .photos(List.of("photo1.jpg", "photo2.jpg"))
                .description("Comfortable running shoes")
                .price(100)
                .inStock(true)
                .products(List.of(
                        Product.builder().id(1L).build(),
                        Product.builder().id(2L).build(),
                        Product.builder().id(3L).build()
                ))
                .build();

        ShoeTypeDto shoeTypeDtoMy = ShoeTypeDto.from(shoeType);
        Assertions.assertEquals(shoeTypeDtoRes, shoeTypeDtoMy);
    }

    @Test
    void testFromForListOfObjects() {
        List<ShoeType> shoeTypeList = List.of(
                ShoeType.builder()
                        .id(1L)
                        .brand("Nike")
                        .model("Air Max")
                        .category(Category.builder().id(1L).build())
                        .color("Black")
                        .photos(List.of("photo1.jpg", "photo2.jpg"))
                        .description("Comfortable running shoes")
                        .price(100)
                        .inStock(true)
                        .products(List.of(
                                Product.builder().id(1L).build(),
                                Product.builder().id(2L).build()
                        ))
                        .build(),
                ShoeType.builder()
                        .id(2L)
                        .brand("Adidas")
                        .model("Superstar")
                        .category(Category.builder().id(2L).build())
                        .color("White")
                        .photos(List.of("photo3.jpg", "photo4.jpg"))
                        .description("Classic sneakers")
                        .price(80)
                        .inStock(false)
                        .products(List.of(
                                Product.builder().id(3L).build(),
                                Product.builder().id(4L).build()
                        ))
                        .build()
        );

        List<ShoeTypeDto> shoeTypeDtoList = ShoeTypeDto.from(shoeTypeList);

        List<ShoeTypeDto> expectedShoeTypeDtoList = List.of(
                ShoeTypeDto.builder()
                        .id(1L)
                        .brand("Nike")
                        .model("Air Max")
                        .categoryId(1L)
                        .color("Black")
                        .photos(List.of("photo1.jpg", "photo2.jpg"))
                        .description("Comfortable running shoes")
                        .price(100)
                        .inStock(true)
                        .productsId(List.of(1L, 2L))
                        .build(),
                ShoeTypeDto.builder()
                        .id(2L)
                        .brand("Adidas")
                        .model("Superstar")
                        .categoryId(2L)
                        .color("White")
                        .photos(List.of("photo3.jpg", "photo4.jpg"))
                        .description("Classic sneakers")
                        .price(80)
                        .inStock(false)
                        .productsId(List.of(3L, 4L))
                        .build()
        );

        Assertions.assertEquals(expectedShoeTypeDtoList, shoeTypeDtoList);
    }
}