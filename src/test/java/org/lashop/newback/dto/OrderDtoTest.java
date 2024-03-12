package org.lashop.newback.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lashop.newback.models.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDtoTest {

    @Test
    void testFromForOneObject() {
        OrderDto orderDtoRes = OrderDto.builder()
                .id(1L)
                .userId(1L)
                .orderDate(LocalDate.now())
                .addressId(1L)
                .cardId(1L)
                .totalSum(100)
                .productListIds(List.of(1L, 2L, 3L))
                .build();

        Orders order = Orders.builder()
                .id(1L)
                .account(Account.builder().id(1L).build())
                .orderDate(LocalDate.now())
                .address(Address.builder().id(1L).build())
                .card(Card.builder().id(1L).build())
                .totalSum(100)
                .productsList(List.of(
                        OrderedProducts.builder().id(1L).build(),
                        OrderedProducts.builder().id(2L).build(),
                        OrderedProducts.builder().id(3L).build()
                ))
                .build();

        OrderDto orderDtoMy = OrderDto.from(order);
        Assertions.assertEquals(orderDtoRes, orderDtoMy);
    }

    @Test
    void testFromForListOfObjects() {
        List<Orders> orderList = List.of(
                Orders.builder()
                        .id(1L)
                        .account(Account.builder().id(1L).build())
                        .orderDate(LocalDate.now())
                        .address(Address.builder().id(1L).build())
                        .card(Card.builder().id(1L).build())
                        .totalSum(100)
                        .productsList(List.of(
                                OrderedProducts.builder().id(1L).build(),
                                OrderedProducts.builder().id(2L).build()
                        ))
                        .build(),
                Orders.builder()
                        .id(2L)
                        .account(Account.builder().id(2L).build())
                        .orderDate(LocalDate.now())
                        .address(Address.builder().id(2L).build())
                        .card(Card.builder().id(2L).build())
                        .totalSum(200)
                        .productsList(List.of(
                                OrderedProducts.builder().id(3L).build(),
                                OrderedProducts.builder().id(4L).build()
                        ))
                        .build()
        );

        List<OrderDto> orderDtoList = OrderDto.from(orderList);

        List<OrderDto> expectedOrderDtoList = List.of(
                OrderDto.builder()
                        .id(1L)
                        .userId(1L)
                        .orderDate(LocalDate.now())
                        .addressId(1L)
                        .cardId(1L)
                        .totalSum(100)
                        .productListIds(List.of(1L, 2L))
                        .build(),
                OrderDto.builder()
                        .id(2L)
                        .userId(2L)
                        .orderDate(LocalDate.now())
                        .addressId(2L)
                        .cardId(2L)
                        .totalSum(200)
                        .productListIds(List.of(3L, 4L))
                        .build()
        );

        Assertions.assertEquals(expectedOrderDtoList, orderDtoList);
    }
}