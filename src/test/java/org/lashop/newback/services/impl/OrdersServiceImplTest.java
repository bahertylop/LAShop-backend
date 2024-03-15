package org.lashop.newback.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.OrderDto;
import org.lashop.newback.models.*;
import org.lashop.newback.repositories.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdersServiceImplTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderedProductsRepository orderedProductsRepository;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMakeOrderSuccess() {
        LocalDate localDate = LocalDate.now();

        OrderDto orderDto = OrderDto.builder()
                .id(1L)
                .orderDate(localDate)
                .addressId(1L)
                .cardId(1L)
                .totalSum(1000)
                .productListIds(List.of(1L))
                .build();

        List<CartDto> cart = new ArrayList<>();
        Long accountId = 1L;

        when(cardRepository.findById(orderDto.getCardId())).thenReturn(Optional.of(Card.builder().id(orderDto.getCardId()).build()));
        when(addressRepository.findById(orderDto.getAddressId())).thenReturn(Optional.of(Address.builder().id(orderDto.getAddressId()).build()));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(Account.builder().id(accountId).build()));

        ordersService.makeOrder(orderDto, cart, accountId);

        verify(ordersRepository, times(1)).save(any(Orders.class));

    }

    @Test
    void testMakeOrderNoCard() {
        LocalDate localDate = LocalDate.now();

        OrderDto orderDto = OrderDto.builder()
                .id(1L)
                .orderDate(localDate)
                .addressId(1L)
                .cardId(1L)
                .totalSum(1000)
                .productListIds(List.of(1L))
                .build();

        List<CartDto> cart = new ArrayList<>();
        Long accountId = 1L;

        when(cardRepository.findById(orderDto.getCardId())).thenReturn(Optional.empty());
        when(addressRepository.findById(orderDto.getAddressId())).thenReturn(Optional.of(Address.builder().id(orderDto.getAddressId()).build()));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(Account.builder().id(accountId).build()));

        Assertions.assertThrows(RuntimeException.class, () -> ordersService.makeOrder(orderDto, cart, accountId));
    }

    @Test
    void testMakeOrderNoAddress() {
        LocalDate localDate = LocalDate.now();

        OrderDto orderDto = OrderDto.builder()
                .id(1L)
                .orderDate(localDate)
                .addressId(1L)
                .cardId(1L)
                .totalSum(1000)
                .productListIds(List.of(1L))
                .build();

        List<CartDto> cart = new ArrayList<>();
        Long accountId = 1L;

        when(cardRepository.findById(orderDto.getCardId())).thenReturn(Optional.of(Card.builder().id(orderDto.getCardId()).build()));
        when(addressRepository.findById(orderDto.getAddressId())).thenReturn(Optional.empty());
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(Account.builder().id(accountId).build()));

        Assertions.assertThrows(RuntimeException.class, () -> ordersService.makeOrder(orderDto, cart, accountId));
    }

    @Test
    void testMakeOrderNoAccount() {
        LocalDate localDate = LocalDate.now();

        OrderDto orderDto = OrderDto.builder()
                .id(1L)
                .orderDate(localDate)
                .addressId(1L)
                .cardId(1L)
                .totalSum(1000)
                .productListIds(List.of(1L))
                .build();

        List<CartDto> cart = new ArrayList<>();
        Long accountId = 1L;

        when(cardRepository.findById(orderDto.getCardId())).thenReturn(Optional.of(Card.builder().id(orderDto.getCardId()).build()));
        when(addressRepository.findById(orderDto.getAddressId())).thenReturn(Optional.of(Address.builder().id(orderDto.getAddressId()).build()));
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> ordersService.makeOrder(orderDto, cart, accountId));
    }

    @Test
    void testGetUserOrders() {
        Long accountId = 1L;

        List<OrderDto> orders = List.of(OrderDto.builder()
                .id(1L)
                        .userId(1L)
                        .addressId(1L)
                        .cardId(1L)
                        .totalSum(1000)
                        .productListIds(List.of(1L))
                .build());

        when(ordersRepository.getOrdersByAccountIdOrderByIdDesc(accountId)).thenReturn(Optional.of(List.of(Orders.builder()
                .id(1)
                        .account(Account.builder().id(1).build())
                        .address(Address.builder().id(1).build())
                        .card(Card.builder().id(1).build())
                        .totalSum(1000)
                        .productsList(List.of(OrderedProducts.builder().id(1).build()))
                .build())));

        List<OrderDto> res = ordersService.getUserOrders(accountId);

        Assertions.assertEquals(orders, res);
    }

    @Test
    void testGetUserOrdersThrowsException() {
        Long accountId = 1L;

        List<OrderDto> orders = List.of(OrderDto.builder()
                .id(1L)
                .userId(1L)
                .addressId(1L)
                .cardId(1L)
                .totalSum(1000)
                .productListIds(List.of(1L))
                .build());

        when(ordersRepository.getOrdersByAccountIdOrderByIdDesc(accountId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> ordersService.getUserOrders(accountId));
    }

    @Test
    void testGetAllOrders() {
        List<OrderDto> orders = List.of(OrderDto.builder()
                .id(1L)
                .userId(1L)
                .addressId(1L)
                .cardId(1L)
                .totalSum(1000)
                .productListIds(List.of(1L))
                .build());

        when(ordersRepository.findAllByOrderByIdDesc()).thenReturn(List.of(Orders.builder()
                .id(1)
                .account(Account.builder().id(1).build())
                .address(Address.builder().id(1).build())
                .card(Card.builder().id(1).build())
                .totalSum(1000)
                .productsList(List.of(OrderedProducts.builder().id(1).build()))
                .build()));

        List<OrderDto> res = ordersService.getAllOrders();

        Assertions.assertEquals(orders, res);
    }
}