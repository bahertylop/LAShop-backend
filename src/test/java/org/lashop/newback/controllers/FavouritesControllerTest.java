package org.lashop.newback.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.CartService;
import org.lashop.newback.services.FavouritesService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FavouritesControllerTest {

    @Mock
    private FavouritesService favouritesService;

    @InjectMocks
    private FavouritesController favouritesController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(favouritesController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetUserFavourites() throws Exception {
        Long userId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(userId).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        List<ShoeTypeDto> list = List.of(
                ShoeTypeDto.builder().id(1).brand("adidas").model("superstar").build(),
                ShoeTypeDto.builder().id(2).brand("nike").model("air max").build()
        );

        when(favouritesService.getFavouritesList(anyLong())).thenReturn(list);

        mockMvc.perform(get("/api/favourites").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(list.size()))
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }
}