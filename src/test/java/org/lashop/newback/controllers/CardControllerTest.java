package org.lashop.newback.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.AddressDto;
import org.lashop.newback.dto.CardDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.CardsService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    @Mock
    private CardsService cardsService;

    @InjectMocks
    private CardController cardController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetUserCards_Success() throws Exception {
        // Arrange
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<CardDto> cards = Collections.singletonList(new CardDto());
        when(cardsService.getUserCards(anyLong())).thenReturn(cards);

        // Act & Assert
        mockMvc.perform(get("/api/cards/get").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(cards.size()));
    }

    @Test
    public void testGetUserCards_Unauthorized() throws Exception {
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = cardController.getUserCards(principal);
        });
    }

    @Test
    public void testGetUserCards_ThrowsException() throws Exception {
        // Arrange
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(cardsService.getUserCards(anyLong())).thenThrow(new RuntimeException("account not found"));

        // Act & Assert
        mockMvc.perform(get("/api/cards/get").principal(principal))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("account not found"));
    }

    @Test
    public void testAddCard_Success() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        CardDto cardDto = new CardDto();
        cardDto.setCardCVV("123");
        cardDto.setCardDate("12/24");
        cardDto.setCardNumber("1234567890123456");
        cardDto.setPaySystem("Visa");

        // Act & Assert
        mockMvc.perform(post("/api/cards/add")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("card added"));

        verify(cardsService, times(1)).addNewCard(anyLong(), eq(cardDto));
    }

    @Test
    public void testAddCard_Unauthorized() throws Exception {
        CardDto cardDto = new CardDto();
        cardDto.setCardCVV("123");
        cardDto.setCardDate("12/24");
        cardDto.setCardNumber("1234567890123456");
        cardDto.setPaySystem("Visa");
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = cardController.addCard(cardDto, principal);
        });
    }

    @Test
    public void testAddCard_EmptyRequestBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;
        // Act & Assert
        mockMvc.perform(post("/api/cards/add").principal(principal)
                .contentType(MediaType.APPLICATION_JSON) // Установить тип контента JSON
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("request has empty body"));

        verify(cardsService, never()).addNewCard(anyLong(), any(CardDto.class));
    }

    @Test
    public void testAddCard_ThrowException() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        CardDto cardDto = new CardDto();
        cardDto.setCardCVV("123");
        cardDto.setCardDate("12/24");
        cardDto.setCardNumber("1234567890123456");
        cardDto.setPaySystem("Visa");

        doThrow(new RuntimeException("exception")).when(cardsService).addNewCard(userDetails.getId(), cardDto);

        mockMvc.perform(post("/api/cards/add").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("exception"));

        verify(cardsService, times(1)).addNewCard(anyLong(), any(CardDto.class));
    }


}