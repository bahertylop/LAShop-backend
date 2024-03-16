package org.lashop.newback.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.CategoryDto;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.CartService;
import org.lashop.newback.services.CategoryService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoriesControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoriesController categoriesController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoriesController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetCategoryShoeTypes() throws Exception {
        Long categoryId = 1L;
        CategoryDto categoryDto = CategoryDto.builder().id(categoryId).name("shoe").image("image").shoeTypeIds(List.of(1L, 2L, 3L)).build();
        List<ShoeTypeDto> shoeTypeDtoList = List.of(
                ShoeTypeDto.builder().id(1).categoryId(categoryId).inStock(true).build(),
                ShoeTypeDto.builder().id(2).categoryId(categoryId).inStock(true).build(),
                ShoeTypeDto.builder().id(3).categoryId(categoryId).inStock(true).build());

        when(categoryService.getCategoryById(categoryId)).thenReturn(categoryDto);
        when(categoryService.getCategoryShoeTypes(categoryId)).thenReturn(shoeTypeDtoList);

        mockMvc.perform(get("/api/categories/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(shoeTypeDtoList.size()))
                .andExpect(content().json(objectMapper.writeValueAsString(shoeTypeDtoList)));

        verify(categoryService).getCategoryById(categoryId);
    }

    @Test
    public void testGetCategoryShoeTypesThrows() throws Exception {
        Long categoryId = 1L;
        // Имитируем поведение categoryService.getCategoryById
        when(categoryService.getCategoryById(categoryId)).thenThrow(new RuntimeException("category not found"));


        // Выполняем запрос
        mockMvc.perform(get("/api/categories/{categoryId}", categoryId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("category not found"));
    }

    @Test
    public void testGetAllCategories() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        List<CategoryDto> categoryDtos = List.of(
                CategoryDto.builder().id(1L).name("shoe").image("image").shoeTypeIds(List.of(1L, 2L, 3L)).build()
        );

        when(categoryService.getAllCategories()).thenReturn(categoryDtos);

        mockMvc.perform(get("/api/adm/categories/all").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(categoryDtos.size()))
                .andExpect(content().json(objectMapper.writeValueAsString(categoryDtos)));
    }

    @Test
    public void testAddNewCategory() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        CategoryDto categoryDto = CategoryDto.builder().name("shoe").image("image").build();

        doNothing().when(categoryService).createCategory(categoryDto.getName(), categoryDto.getImage());

        mockMvc.perform(post("/api/adm/categories/add")
                        .principal(principal)
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("category added"));

        verify(categoryService).createCategory(categoryDto.getName(), categoryDto.getImage());
    }


    @Test
    public void testAddNewCategory_EmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/adm/categories/add")
                        .principal(principal)
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }

    @Test
    public void testAddNewCategory_Throws() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        CategoryDto categoryDto = CategoryDto.builder().name("shoe").image("image").build();

        doThrow(new RuntimeException("category not found")).when(categoryService).createCategory(categoryDto.getName(), categoryDto.getImage());

        mockMvc.perform(post("/api/adm/categories/add")
                        .principal(principal)
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("category not found"));

        verify(categoryService).createCategory(categoryDto.getName(), categoryDto.getImage());
    }

    @Test
    void testDeleteCategory() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        CategoryDto categoryDto = CategoryDto.builder().name("shoe").image("image").build();

        doNothing().when(categoryService).deleteCategoryAndTakeTypesNotInStock(categoryDto.getName());

        mockMvc.perform(post("/api/adm/categories/delete")
                        .principal(principal)
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("category deleted"));
    }

    @Test
    void testDeleteCategory_EmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/adm/categories/delete")
                        .principal(principal)
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }
}
