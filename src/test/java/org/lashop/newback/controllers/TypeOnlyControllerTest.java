package org.lashop.newback.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.dto.requests.SizeQuantity;
import org.lashop.newback.dto.responses.TypeOnlyResponse;
import org.lashop.newback.dto.responses.TypeOnlyResponseAdm;
import org.lashop.newback.models.Account;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TypeOnlyControllerTest {
    @Mock
    private ShoeTypeService shoeTypeService;

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private TypeOnlyController typeOnlyController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(typeOnlyController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetShoeTypeCard() throws Exception {
        Long shoeTypeId = 1L;

        ShoeTypeDto shoeTypeDto = ShoeTypeDto.builder().id(1).categoryId(1).brand("adidas").build();
        List<ShoeTypeDto> relatedProducts = List.of(
                ShoeTypeDto.builder().id(2).brand("adidas").build(),
                ShoeTypeDto.builder().id(3).brand("adidas").build(),
                ShoeTypeDto.builder().id(4).brand("adidas").build()
        );
        List<Double> sizes = List.of(40.0, 41.0, 42.0);

        TypeOnlyResponse resp = TypeOnlyResponse.builder()
                .shoeTypeDto(shoeTypeDto)
                .relatedShoeTypes(relatedProducts)
                .sizes(sizes)
                .build();

        when(shoeTypeService.getTypeById(shoeTypeId)).thenReturn(shoeTypeDto);
        when(categoryService.getCategoryShoeTypesLimited(anyLong(), anyInt())).thenReturn(relatedProducts);
        when(productService.getAllSizesForType(shoeTypeId)).thenReturn(sizes);

        mockMvc.perform(get("/api/products/{shoeTypeId}", shoeTypeId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(resp)));

        verify(shoeTypeService, times(1)).getTypeById(shoeTypeId);
        verify(categoryService).getCategoryShoeTypesLimited(anyLong(), anyInt());
        verify(productService).getAllSizesForType(shoeTypeId);
    }

    @Test
    public void testGetShoeTypeCardThrows() throws Exception {
        Long shoeTypeId = 1L;

        doThrow(new RuntimeException("exception")).when(shoeTypeService).getTypeById(shoeTypeId);

        mockMvc.perform(get("/api/products/{shoeTypeId}", shoeTypeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("exception"));

        verify(shoeTypeService, times(1)).getTypeById(shoeTypeId);
    }

    @Test
    public void testGetShoeTypeCardAdm() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        ShoeTypeDto shoeTypeDto = ShoeTypeDto.builder().id(1).categoryId(1).brand("adidas").build();
        List<Double> sizes = List.of(40.0, 41.0, 42.0);

        TypeOnlyResponseAdm resp = TypeOnlyResponseAdm.builder()
                .shoeTypeDto(shoeTypeDto)
                .sizes(sizes)
                .build();

        when(shoeTypeService.getTypeById(shoeTypeId)).thenReturn(shoeTypeDto);
        when(productService.getAllSizesForType(shoeTypeId)).thenReturn(sizes);

        mockMvc.perform(get("/api/adm/products/{shoeTypeId}", shoeTypeId).principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(resp)));
    }

    @Test
    public void testGetShoeTypeCardAdmThrows() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        doThrow(new RuntimeException("exception")).when(shoeTypeService).getTypeById(shoeTypeId);

        mockMvc.perform(get("/api/adm/products/{shoeTypeId}", shoeTypeId).principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("exception"));
    }

    @Test
    public void testUpdateShoeType() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        ShoeTypeDto dto = ShoeTypeDto.builder()
                .id(shoeTypeId)
                .brand("brand")
                .model("model")
                .color("color")
                .description("description")
                .price(100)
                .build();
        doNothing().when(shoeTypeService).updateShoeType(dto);

        mockMvc.perform(post("/api/adm/products/{shoeTypeId}/update", shoeTypeId)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("shoe type updated"));

        verify(shoeTypeService, times(1)).updateShoeType(dto);
    }

    @Test
    public void testUpdateShoeTypeEmptyBody() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/adm/products/{shoeTypeId}/update", shoeTypeId)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }

    @Test
    public void testUpdateShoeTypeThrows() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        ShoeTypeDto dto = ShoeTypeDto.builder()
                .id(shoeTypeId)
                .brand("brand")
                .model("model")
                .color("color")
                .description("description")
                .price(100)
                .build();
        doThrow(new RuntimeException("exception")).when(shoeTypeService).updateShoeType(dto);

        mockMvc.perform(post("/api/adm/products/{shoeTypeId}/update", shoeTypeId)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("exception"));

        verify(shoeTypeService, times(1)).updateShoeType(dto);
    }

    @Test
    public void testAddShoePairs() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        SizeQuantity sizeQuantity = SizeQuantity.builder().quantity(2).size(40.0).build();

        doNothing().when(productService).addSomeNewProducts(shoeTypeId, sizeQuantity.getSize(), sizeQuantity.getQuantity());

        mockMvc.perform(post("/api/adm/products/{shoeTypeId}/add", shoeTypeId)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sizeQuantity))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("pairs added"));

        verify(productService, times(1)).addSomeNewProducts(shoeTypeId, sizeQuantity.getSize(), sizeQuantity.getQuantity());
    }

    @Test
    public void testAddShoePairsEmptyBody() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/adm/products/{shoeTypeId}/add", shoeTypeId)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }

    @Test
    public void testAddShoePairsThrows() throws Exception {
        Long shoeTypeId = 1L;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        SizeQuantity sizeQuantity = SizeQuantity.builder().quantity(2).size(40.0).build();

        doThrow(new RuntimeException("exception")).when(productService).addSomeNewProducts(shoeTypeId, sizeQuantity.getSize(), sizeQuantity.getQuantity());

        mockMvc.perform(post("/api/adm/products/{shoeTypeId}/add", shoeTypeId)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sizeQuantity))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("exception"));

        verify(productService, times(1)).addSomeNewProducts(shoeTypeId, sizeQuantity.getSize(), sizeQuantity.getQuantity());
    }



}