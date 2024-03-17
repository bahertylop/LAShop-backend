package org.lashop.newback.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.CategoryService;
import org.lashop.newback.services.ProductService;
import org.lashop.newback.services.ShoeTypeService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
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
class TypesControllerTest {

    @Mock
    private ShoeTypeService shoeTypeService;

    @InjectMocks
    private TypesController typesController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(typesController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetShoeTypesInStock() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        List<ShoeTypeDto> dtos = List.of(
                ShoeTypeDto.builder().id(1).brand("adidas").build(),
                ShoeTypeDto.builder().id(2).brand("adidas").build());

        when(shoeTypeService.getShoeTypesIsInStock(true)).thenReturn(dtos);

        mockMvc.perform(get("/api/adm/types/inStock")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(dtos.size()))
                .andExpect(content().json(objectMapper.writeValueAsString(dtos)));

        verify(shoeTypeService, times(1)).getShoeTypesIsInStock(true);
    }

    @Test
    public void testGetShoeTypesNotInStock() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        List<ShoeTypeDto> dtos = List.of(
                ShoeTypeDto.builder().id(1).brand("adidas").build(),
                ShoeTypeDto.builder().id(2).brand("adidas").build());

        when(shoeTypeService.getShoeTypesIsInStock(false)).thenReturn(dtos);

        mockMvc.perform(get("/api/adm/types/notInStock")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(dtos.size()))
                .andExpect(content().json(objectMapper.writeValueAsString(dtos)));

        verify(shoeTypeService, times(1)).getShoeTypesIsInStock(false);
    }

    @Test
    public void testTakeNotInStock() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        doNothing().when(shoeTypeService).changeInStock(shoeTypeId, false);

        mockMvc.perform(post("/api/adm/types/takeNotInStock/{shoeTypeId}", shoeTypeId)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("take inStock false"));
    }

    @Test
    public void testTakeNotInStockThrows() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        doThrow(RuntimeException.class).when(shoeTypeService).changeInStock(shoeTypeId, false);

        mockMvc.perform(post("/api/adm/types/takeNotInStock/{shoeTypeId}", shoeTypeId)
                        .principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("shoe type not found"));
    }

    @Test
    public void testTakeInStock() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        doNothing().when(shoeTypeService).changeInStock(shoeTypeId, true);

        mockMvc.perform(post("/api/adm/types/takeInStock/{shoeTypeId}", shoeTypeId)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("take inStock true"));
    }

    @Test
    public void testTakeInStockThrows() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        doThrow(RuntimeException.class).when(shoeTypeService).changeInStock(shoeTypeId, true);

        mockMvc.perform(post("/api/adm/types/takeInStock/{shoeTypeId}", shoeTypeId)
                        .principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("shoe type not found"));
    }

    @Test
    public void testAddNewShoeType() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        ShoeTypeDto dto = ShoeTypeDto.builder()
                .id(1L)
                .brand("brand")
                .model("model")
                .color("color")
                .description("description")
                .price(100)
                .build();

        doNothing().when(shoeTypeService).createNewShoeType(dto);

        mockMvc.perform(post("/api/adm/types/add")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("new shoe type added"));
    }


    @Test
    public void testAddNewShoeTypeThrows() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        ShoeTypeDto dto = ShoeTypeDto.builder()
                .id(1L)
                .brand("brand")
                .model("model")
                .color("color")
                .description("description")
                .price(100)
                .build();

        doThrow(new RuntimeException("exception")).when(shoeTypeService).createNewShoeType(dto);

        mockMvc.perform(post("/api/adm/types/add")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("exception"));
    }

    @Test
    public void testAddNewShoeTypeEmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/adm/types/add")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }
}