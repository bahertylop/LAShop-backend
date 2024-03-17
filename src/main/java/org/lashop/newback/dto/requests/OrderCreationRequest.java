package org.lashop.newback.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.OrderDto;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreationRequest {

    private OrderDto orderDto;
    private List<CartDto> cart;
}
