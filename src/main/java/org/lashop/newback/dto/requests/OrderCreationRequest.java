package org.lashop.newback.dto.requests;

import lombok.Builder;
import lombok.Data;
import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.OrderDto;

import java.util.List;


@Data
@Builder
public class OrderCreationRequest {

    private OrderDto orderDto;
    private List<CartDto> cart;
}
