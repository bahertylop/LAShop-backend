package org.lashop.newback.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.weaver.bcel.BcelShadow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.dto.requests.CurrentShoe;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.CartService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.mockito.Mockito.get;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetUserCart() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        List<CartDto> list = List.of(CartDto.builder().id(1).size(40).shoeType(ShoeTypeDto.builder().id(1).build()).quantity(2).build(),
                CartDto.builder().id(2).size(42).shoeType(ShoeTypeDto.builder().id(1).build()).quantity(2).build());

        doNothing().when(cartService).checkStockOfProductsCart(userDetails.getId());
        when(cartService.takeCart(userDetails.getId())).thenReturn(list);

        mockMvc.perform(get("/api/cart").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(list.size()))
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }

    @Test
    public void testGetUserCart_Unauthorized() throws Exception {
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = cartController.getUserCart(principal);
        });
    }

    @Test
    public void testMinusQuantity() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        CurrentShoe shoe = CurrentShoe.builder().shoeTypeId(1L).size(40.0).build();

        doNothing().when(cartService).minusCount(shoe.getShoeTypeId(), shoe.getSize(), userDetails.getId());
        mockMvc.perform(post("/api/cart/minus")
                        .principal(principal)
                        .content(objectMapper.writeValueAsString(shoe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("minused"));
    }

    @Test
    public void testMinusQuantity_Unauthorized() throws Exception {
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = cartController.minusQuantity(CurrentShoe.builder().build(), principal);
        });
    }

    @Test
    public void testMinusQuantity_EmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/cart/minus")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON) // Установить тип контента JSON
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }


    @Test
    public void testPlusQuantity() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        CurrentShoe shoe = CurrentShoe.builder().shoeTypeId(1L).size(40.0).build();

        doNothing().when(cartService).plusCount(shoe.getShoeTypeId(), shoe.getSize(), userDetails.getId());
        mockMvc.perform(post("/api/cart/plus")
                        .principal(principal)
                        .content(objectMapper.writeValueAsString(shoe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("plused"));
    }

    @Test
    public void testPlusQuantity_Unauthorized() throws Exception {
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = cartController.plusQuantity(CurrentShoe.builder().build(), principal);
        });
    }

    @Test
    public void testPlusQuantity_EmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/cart/plus")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON) // Установить тип контента JSON
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }

    @Test
    public void testDeletePositionFromCart() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        CurrentShoe shoe = CurrentShoe.builder().shoeTypeId(1L).size(40.0).build();

        doNothing().when(cartService).plusCount(shoe.getShoeTypeId(), shoe.getSize(), userDetails.getId());
        mockMvc.perform(post("/api/cart/delete")
                        .principal(principal)
                        .content(objectMapper.writeValueAsString(shoe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("deleted"));
    }

    @Test
    public void testDeletePositionFromCart_Unauthorized() throws Exception {
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = cartController.deletePositionFromCart(CurrentShoe.builder().build(), principal);
        });
    }

    @Test
    public void testDeletePositionFromCart_EmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/cart/delete")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON) // Установить тип контента JSON
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }

    @Test
    public void testAddPositionToCart() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        CurrentShoe shoe = CurrentShoe.builder().shoeTypeId(1L).size(40.0).build();

        doNothing().when(cartService).plusCount(shoe.getShoeTypeId(), shoe.getSize(), userDetails.getId());
        mockMvc.perform(post("/api/cart/add")
                        .principal(principal)
                        .content(objectMapper.writeValueAsString(shoe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("added"));
    }

    @Test
    public void testAddPositionToCart2() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        CurrentShoe shoe = CurrentShoe.builder().shoeTypeId(1L).size(40.0).build();

        doThrow(new RuntimeException()).when(cartService).addToCart(shoe.getShoeTypeId(), shoe.getSize(), userDetails.getId());
        mockMvc.perform(post("/api/cart/add")
                        .principal(principal)
                        .content(objectMapper.writeValueAsString(shoe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("account or shoeType not found, or not in stock"));
    }

    @Test
    public void testAddPositionToCart_Unauthorized() throws Exception {
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = cartController.addToCart(CurrentShoe.builder().build(), principal);
        });
    }

    @Test
    public void testAddPositionToCart_EmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/cart/add")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON) // Установить тип контента JSON
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }
}