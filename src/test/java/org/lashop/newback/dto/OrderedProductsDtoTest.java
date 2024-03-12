package org.lashop.newback.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lashop.newback.models.OrderedProducts;
import org.lashop.newback.models.Orders;
import org.lashop.newback.models.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderedProductsDtoTest {

    @Test
    void testFromForOneObject() {
        OrderedProductsDto orderedProductsDtoRes = OrderedProductsDto.builder()
                .id(1)
                .productId(1)
                .orderId(1)
                .build();

        OrderedProducts orderedProducts = OrderedProducts.builder()
                .id(1)
                .product(Product.builder().id(1).build())
                .order(Orders.builder().id(1).build())
                .build();

        OrderedProductsDto orderedProductsDtoMy = OrderedProductsDto.from(orderedProducts);
        Assertions.assertEquals(orderedProductsDtoRes, orderedProductsDtoMy);
    }

    @Test
    void testFromForListOfObjects() {
        List<OrderedProducts> orderedProductsList = List.of(
                OrderedProducts.builder()
                        .id(1)
                        .product(Product.builder().id(1).build())
                        .order(Orders.builder().id(1).build())
                        .build(),
                OrderedProducts.builder()
                        .id(2)
                        .product(Product.builder().id(2).build())
                        .order(Orders.builder().id(2).build())
                        .build()
        );

        List<OrderedProductsDto> orderedProductsDtoList = OrderedProductsDto.from(orderedProductsList);

        List<OrderedProductsDto> expectedOrderedProductsDtoList = List.of(
                OrderedProductsDto.builder()
                        .id(1)
                        .productId(1)
                        .orderId(1)
                        .build(),
                OrderedProductsDto.builder()
                        .id(2)
                        .productId(2)
                        .orderId(2)
                        .build()
        );

        Assertions.assertEquals(expectedOrderedProductsDtoList, orderedProductsDtoList);
    }
}