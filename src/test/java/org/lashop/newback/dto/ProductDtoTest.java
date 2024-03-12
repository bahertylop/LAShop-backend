package org.lashop.newback.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lashop.newback.models.Product;
import org.lashop.newback.models.ShoeType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDtoTest {

    @Test
    void testFromForOneObject() {
        ProductDto productDtoRes = ProductDto.builder()
                .id(1L)
                .typeId(1L)
                .size(10.5)
                .sold(false)
                .build();

        Product product = Product.builder()
                .id(1L)
                .shoeType(ShoeType.builder().id(1L).build())
                .size(10.5)
                .sold(false)
                .build();

        ProductDto productDtoMy = ProductDto.from(product);
        Assertions.assertEquals(productDtoRes, productDtoMy);
    }

    @Test
    void testFromForListOfObjects() {
        List<Product> productList = List.of(
                Product.builder()
                        .id(1L)
                        .shoeType(ShoeType.builder().id(1L).build())
                        .size(10.5)
                        .sold(false)
                        .build(),
                Product.builder()
                        .id(2L)
                        .shoeType(ShoeType.builder().id(2L).build())
                        .size(9.0)
                        .sold(true)
                        .build()
        );

        List<ProductDto> productDtoList = ProductDto.from(productList);

        List<ProductDto> expectedProductDtoList = List.of(
                ProductDto.builder()
                        .id(1L)
                        .typeId(1L)
                        .size(10.5)
                        .sold(false)
                        .build(),
                ProductDto.builder()
                        .id(2L)
                        .typeId(2L)
                        .size(9.0)
                        .sold(true)
                        .build()
        );

        Assertions.assertEquals(expectedProductDtoList, productDtoList);
    }
}