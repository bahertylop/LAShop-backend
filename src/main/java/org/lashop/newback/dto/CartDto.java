package org.lashop.newback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lashop.newback.models.Cart;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

    private long id;
    private ShoeTypeDto shoeType;
    private double size;
    private int quantity;
    private long accountId;

    public static CartDto from(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .shoeType(ShoeTypeDto.from(cart.getShoeType()))
                .size(cart.getSize())
                .quantity(cart.getQuantity())
                .accountId(cart.getAccount().getId())
                .build();
    }

    public static List<CartDto> from(List<Cart> carts) {
        return carts.stream().map(CartDto::from).toList();
    }

}
