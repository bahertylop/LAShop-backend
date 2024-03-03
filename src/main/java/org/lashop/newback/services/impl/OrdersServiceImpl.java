package org.lashop.newback.services.impl;

import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.OrderDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.OrderedProducts;
import org.lashop.newback.models.Orders;
import org.lashop.newback.models.Product;
import org.lashop.newback.repositories.*;
import org.lashop.newback.services.OrdersService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final CartRepository cartRepository;
    private final CardRepository cardRepository;
    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final OrderedProductsRepository orderedProductsRepository;


    // надо тестить
    // проблемы с номером заказа в заказаныых товарах,
    // проблемы с приходом количества товаров
    @Override
    public void makeOrder(OrderDto orderDto, List<CartDto> cart, Long accountId) {
        Orders newOrder = Orders.builder()
                .orderDate(LocalDate.now())
                .card(cardRepository.findById(orderDto.getCardId()).orElseThrow(() -> new RuntimeException("card not found")))
                .address(addressRepository.findById(orderDto.getAddressId()).orElseThrow(() -> new RuntimeException("address not found")))
                .account(accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("account not found")))
                .totalSum(orderDto.getTotalSum())
                .build();

        ordersRepository.save(newOrder);

        for (CartDto cartDto : cart) {
            List<Product> orderedProducts = productRepository.
                    findTopNByShoeTypeIdAndSizeAndSoldFalseOrderById(cartDto.getShoeType().getId(),
                            cartDto.getSize(),
                            PageRequest.of(0, cartDto.getQuantity()));
            if (orderedProducts.size() != cartDto.getQuantity()) {
                ordersRepository.deleteById(newOrder.getId());
                throw new RuntimeException("products not in stock");
            }
            for (Product product : orderedProducts) {
                productRepository.updateSoldStatusById(product.getId());
                OrderedProducts orderedProduct = OrderedProducts.builder()
                        .order(newOrder)
                        .product(product)
                        .build();
                orderedProductsRepository.save(orderedProduct);
            }
        }
        cartRepository.deleteByAccountId(accountId);
    }

    @Override
    public List<OrderDto> getUserOrders(Long accountId) {
        return OrderDto.from(ordersRepository.getOrdersByAccountIdOrderByIdDesc(accountId)
                .orElseThrow(() -> new RuntimeException("account not found")));
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return OrderDto.from(ordersRepository.findAllByOrderByIdDesc());
    }
}
