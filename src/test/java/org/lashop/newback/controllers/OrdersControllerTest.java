package org.lashop.newback.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.AddressDto;
import org.lashop.newback.dto.CardDto;
import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.OrderDto;
import org.lashop.newback.dto.requests.OrderCreationRequest;
import org.lashop.newback.dto.responses.HomeResponse;
import org.lashop.newback.dto.responses.InfoToOrder;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.Address;
import org.lashop.newback.models.Cart;
import org.lashop.newback.services.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrdersControllerTest {

    @Mock
    private CardsService cardsService;

    @Mock
    private AddressService addressService;

    @Mock
    private OrdersService ordersService;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrdersController ordersController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ordersController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetInfoForOrder() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        List<AddressDto> addressDtos = List.of(
                AddressDto.builder().address("qwertyuiop").build(),
                AddressDto.builder().address("1234567890").build()
        );
        List<CardDto> cardDtos = List.of(
                CardDto.builder().id(1).build(),
                CardDto.builder().id(2).build()
        );
        List<CartDto> cartDtos = List.of(
                CartDto.builder().id(1).build(),
                CartDto.builder().id(2).build()
        );

        when(addressService.getAllAddresses(userDetails.getId())).thenReturn(addressDtos);
        when(cardsService.getUserCards(userDetails.getId())).thenReturn(cardDtos);
        when(cartService.takeCart(userDetails.getId())).thenReturn(cartDtos);

        InfoToOrder info = InfoToOrder.builder().cart(cartDtos).cards(cardDtos).addresses(addressDtos).build();

        mockMvc.perform(get("/api/order/info").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(info)));
    }

    @Test
    public void testGetInfoForOrder_Unauthorized() {
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = ordersController.getInfoForOrder(principal);
        });
    }

    @Test
    public void testGetInfoForOrderThrows() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        List<AddressDto> addressDtos = List.of(
                AddressDto.builder().address("qwertyuiop").build(),
                AddressDto.builder().address("1234567890").build()
        );

        when(addressService.getAllAddresses(userDetails.getId())).thenThrow(new RuntimeException("account not found"));

        mockMvc.perform(get("/api/order/info")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("account not found"));
    }

    @Test
    void testCreateOrder() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        OrderDto orderDto = OrderDto.builder().cardId(1L).addressId(1L).totalSum(2000).build();
        List<CartDto> carts = List.of(CartDto.builder().id(1).build());
        OrderCreationRequest request = OrderCreationRequest.builder().cart(carts).orderDto(orderDto).build();

        doNothing().when(ordersService).makeOrder(request.getOrderDto(), request.getCart(), userDetails.getId());

        mockMvc.perform(post("/api/order/create")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("order created"));
    }

    @Test
    public void testCreateOrder_Unauthorized() {
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = ordersController.createOrder(OrderCreationRequest.builder().build(), principal);
        });
    }

    @Test
    void testCreateOrderEmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/order/create")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }

    @Test
    void testCreateOrderThrows() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        OrderDto orderDto = OrderDto.builder().cardId(1L).addressId(1L).totalSum(2000).build();
        List<CartDto> carts = List.of(CartDto.builder().id(1).build());
        OrderCreationRequest request = OrderCreationRequest.builder().cart(carts).orderDto(orderDto).build();

        doThrow(new RuntimeException("account not found")).when(ordersService).makeOrder(request.getOrderDto(), request.getCart(), userDetails.getId());

        mockMvc.perform(post("/api/order/create")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("account not found"));
    }

    @Test
    void testGetAllOrders() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().role(Account.Role.ADMIN).id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        List<OrderDto> orders = List.of(
                OrderDto.builder().id(1L).totalSum(200).build(),
                OrderDto.builder().id(2L).totalSum(200).build()
        );

        when(ordersService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/adm/orders").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(orders.size()))
                .andExpect(content().json(objectMapper.writeValueAsString(orders)));
    }
}