package org.lashop.newback.services;

import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.OrderDto;

import java.util.List;

public interface OrdersService {

    void makeOrder(OrderDto orderDto, List<CartDto> cart, Long accountId);

    List<OrderDto> getUserOrders(Long accountId);

    List<OrderDto> getAllOrders();
}
