package org.lashop.newback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lashop.newback.models.OrderedProducts;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderedProductsDto {

    private long id;
    private long productId;
    private long orderId;

    public static OrderedProductsDto from(OrderedProducts ordered) {
        return OrderedProductsDto.builder()
                .id(ordered.getId())
                .productId(ordered.getProduct().getId())
                .orderId(ordered.getOrder().getId())
                .build();
    }

    public static List<OrderedProductsDto> from(List<OrderedProducts> orderedProducts) {
        return orderedProducts.stream().map(OrderedProductsDto::from).toList();
    }
}