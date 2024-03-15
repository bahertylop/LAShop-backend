package org.lashop.newback.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.Category;
import org.lashop.newback.models.Product;
import org.lashop.newback.models.ShoeType;
import org.lashop.newback.repositories.CategoryRepository;
import org.lashop.newback.repositories.ShoeTypeRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ShoeTypeServiceImplTest {

    @Mock
    private ShoeTypeRepository shoeTypeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ShoeTypeServiceImpl shoeTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTypesList() {
        List<ShoeType> shoeTypes = List.of(
                ShoeType.builder()
                        .id(1)
                        .brand("brand1")
                        .model("model1")
                        .category(Category.builder().id(1L).build())
                        .color("black")
                        .photos(List.of("photo1", "photo2"))
                        .description("desc")
                        .price(100)
                        .inStock(true)
                        .products(List.of(Product.builder().id(1).build()))
                        .build(),
                ShoeType.builder()
                        .id(2)
                        .brand("brand2")
                        .model("model2")
                        .category(Category.builder().id(1L).build())
                        .color("black")
                        .photos(List.of("photo3", "photo4"))
                        .description("desc2")
                        .price(200)
                        .inStock(false)
                        .products(List.of(Product.builder().id(2).build()))
                        .build()
        );

        when(shoeTypeRepository.findAll()).thenReturn(shoeTypes);

        List<ShoeTypeDto> result = shoeTypeService.getTypesList();

        assertEquals(ShoeTypeDto.from(shoeTypes), result);
    }

    @Test
    void testGetTypeById_ShouldReturnShoeTypeDto() {
        long typeId = 1L;
        ShoeType shoeType = ShoeType.builder()
                .id(1)
                .brand("brand1")
                .model("model1")
                .category(Category.builder().id(1L).build())
                .color("black")
                .photos(List.of("photo1", "photo2"))
                .description("desc")
                .price(100)
                .inStock(true)
                .products(List.of(Product.builder().id(1).build()))
                .build();

        when(shoeTypeRepository.findById(typeId)).thenReturn(Optional.of(shoeType));

        // Act
        ShoeTypeDto result = shoeTypeService.getTypeById(typeId);

        // Assert
        assertNotNull(result);
        assertEquals(ShoeTypeDto.from(shoeType), result);
    }

    @Test
    void testGetTypeById_ShouldThrowException_WhenShoeTypeNotFound() {
        long typeId = 1L;

        when(shoeTypeRepository.findById(typeId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> shoeTypeService.getTypeById(typeId));
    }

    @Test
    void testDeleteShoeType() {
        long typeId = 1L;
        shoeTypeService.deleteShoeType(typeId);

        verify(shoeTypeRepository, times(1)).deleteById(typeId);
    }

    @Test
    void testCreateNewShoeType() {
        ShoeTypeDto shoeTypeDto = new ShoeTypeDto();
        shoeTypeDto.setCategoryId(1L);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category()));

        shoeTypeService.createNewShoeType(shoeTypeDto);

        verify(shoeTypeRepository, times(1)).save(any(ShoeType.class));
    }

    @Test
    void testUpdateShoeType() {
        ShoeTypeDto shoeTypeDto = ShoeTypeDto.builder()
                .brand("brand2")
                .model("model2")
                .categoryId(1)
                .color("black")
                .photos(List.of("photo3", "photo4"))
                .description("desc2")
                .price(200)
                .inStock(false)
                .productsId(List.of(1L,2L,3L))
                .build();

        when(shoeTypeRepository.findById(anyLong())).thenReturn(Optional.of(ShoeType.builder().id(1).build()));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(Category.builder().id(1).build()));

        shoeTypeService.updateShoeType(shoeTypeDto);

        verify(shoeTypeRepository, times(1)).save(any(ShoeType.class));
    }

    @Test
    void testUpdateShoeTypeThrowsException() {
        ShoeTypeDto shoeTypeDto = ShoeTypeDto.builder()
                .brand("brand2")
                .model("model2")
                .categoryId(1)
                .color("black")
                .photos(List.of("photo3", "photo4"))
                .description("desc2")
                .price(200)
                .inStock(false)
                .productsId(List.of(1L,2L,3L))
                .build();

        when(shoeTypeRepository.findById(anyLong())).thenReturn(Optional.of(ShoeType.builder().id(1).build()));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            shoeTypeService.updateShoeType(shoeTypeDto);
        });
    }

    @Test
    void testGetShoeTypesIsInStock() {
        List<ShoeType> shoeTypes = List.of(
                ShoeType.builder()
                        .id(1)
                        .brand("brand1")
                        .model("model1")
                        .category(Category.builder().id(1L).build())
                        .color("black")
                        .photos(List.of("photo1", "photo2"))
                        .description("desc")
                        .price(100)
                        .inStock(true)
                        .products(List.of(Product.builder().id(1).build()))
                        .build(),
                ShoeType.builder()
                        .id(2)
                        .brand("brand2")
                        .model("model2")
                        .category(Category.builder().id(1L).build())
                        .color("black")
                        .photos(List.of("photo3", "photo4"))
                        .description("desc2")
                        .price(200)
                        .inStock(false)
                        .products(List.of(Product.builder().id(2).build()))
                        .build()
        );

        when(shoeTypeRepository.getShoeTypesByInStock(true)).thenReturn(shoeTypes);

        List<ShoeTypeDto> result = shoeTypeService.getShoeTypesIsInStock(true);

        assertEquals(ShoeTypeDto.from(shoeTypes), result);
    }

    @Test
    void testChangeInStock_ShouldUpdateInStock_WhenShoeTypeExists() {
        ShoeType shoeType =  ShoeType.builder()
                .id(2)
                .brand("brand2")
                .model("model2")
                .category(Category.builder().id(1L).build())
                .color("black")
                .photos(List.of("photo3", "photo4"))
                .description("desc2")
                .price(200)
                .inStock(false)
                .products(List.of(Product.builder().id(2).build()))
                .build();

        when(shoeTypeRepository.findById(shoeType.getId())).thenReturn(Optional.of(shoeType));

        shoeTypeService.changeInStock(shoeType.getId(), true);

        verify(shoeTypeRepository, times(1)).changeInStockById(shoeType.getId(), true);
    }

    @Test
    void testChangeInStock_ShouldThrowException_WhenShoeTypeNotFound() {
        long shoeTypeId = 1L;

        when(shoeTypeRepository.findById(shoeTypeId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> shoeTypeService.changeInStock(shoeTypeId, true));
    }
}