package org.lashop.newback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lashop.newback.models.OrderedProducts;
import org.lashop.newback.models.Orders;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private long id;
    private long userId;
    private LocalDate orderDate;
    private long addressId;
    private long cardId;
    private int totalSum;
    private List<Long> productListIds;

    public static OrderDto from(Orders order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getAccount().getId())
                .orderDate(order.getOrderDate())
                .addressId(order.getAddress().getId())
                .cardId(order.getCard().getId())
                .totalSum(order.getTotalSum())
                .productListIds(order.getProductsList().stream().map(OrderedProducts::getId).toList())
                .build();
    }

    public static List<OrderDto> from(List<Orders> orders) {
        return orders.stream().map(OrderDto::from).toList();
    }

}
