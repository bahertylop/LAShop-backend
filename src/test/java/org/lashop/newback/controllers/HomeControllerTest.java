package org.lashop.newback.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lashop.newback.dto.CategoryDto;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.dto.responses.HomeResponse;
import org.lashop.newback.services.CategoryService;
import org.lashop.newback.services.ShoeTypeService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private ShoeTypeService shoeTypeService;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllModels() throws Exception {
        Long categoryId = 1L;
        List<ShoeTypeDto> shoeTypeDtoList = List.of(
                ShoeTypeDto.builder().id(1).categoryId(categoryId).inStock(true).build(),
                ShoeTypeDto.builder().id(2).categoryId(categoryId).inStock(true).build(),
                ShoeTypeDto.builder().id(3).categoryId(categoryId).inStock(true).build());

        List<CategoryDto> categoryDtoList = List.of(
                CategoryDto.builder().id(categoryId).name("shoe").image("image").shoeTypeIds(List.of(1L, 2L, 3L)).build()
        );

        when(shoeTypeService.getTypesList()).thenReturn(shoeTypeDtoList);
        when(categoryService.getAllCategories()).thenReturn(categoryDtoList);

        mockMvc.perform(get("/api/home"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(HomeResponse.builder().shoeTypes(shoeTypeDtoList).categories(categoryDtoList).build())));
    }

    
}