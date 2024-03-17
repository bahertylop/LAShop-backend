package org.lashop.newback.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.dto.OrderDto;
import org.lashop.newback.dto.responses.ProfileResponse;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.AccountService;
import org.lashop.newback.services.FavouritesService;
import org.lashop.newback.services.OrdersService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private OrdersService ordersService;

    @InjectMocks
    private ProfileController profileController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetUserInfo() throws Exception {
        Long userId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(userId).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        ProfileResponse response = ProfileResponse.builder()
                .accountDto(AccountDto.builder().id(1).email("email").firstName("name").build())
                .orders(List.of(OrderDto.builder().id(1L).userId(1L).addressId(1L).build(),
                        OrderDto.builder().id(2L).userId(1L).addressId(2L).build()))
                .build();

        when(accountService.getAccount(userId)).thenReturn(AccountDto.builder().id(1).email("email").firstName("name").build());
        when(ordersService.getUserOrders(userId)).thenReturn(List.of(OrderDto.builder().id(1L).userId(1L).addressId(1L).build(),
                OrderDto.builder().id(2L).userId(1L).addressId(2L).build()));

        mockMvc.perform(get("/api/profile").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void testGetUserInfoUnauthorized() {
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = profileController.getUserInfo(principal);
        });
    }

    @Test
    public void testGetUserInfoThrows() throws Exception {
        Long userId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(userId).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        doThrow(RuntimeException.class).when(accountService).getAccount(userId);

        mockMvc.perform(get("/api/profile").principal(principal))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("account not found"));

    }
}
