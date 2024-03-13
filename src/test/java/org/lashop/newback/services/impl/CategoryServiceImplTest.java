package org.lashop.newback.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lashop.newback.dto.CategoryDto;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.Category;
import org.lashop.newback.models.Product;
import org.lashop.newback.models.ShoeType;
import org.lashop.newback.repositories.CategoryRepository;
import org.lashop.newback.repositories.ShoeTypeRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ShoeTypeRepository shoeTypeRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        List<Category> categories = new ArrayList<>();
        ShoeType shoeType = ShoeType.builder()
                .id(1)
                .build();
        categories.add(Category.builder().id(1L).name("Category1").products(List.of(shoeType)).image("image1").build());
        categories.add(Category.builder().id(2L).name("Category2").products(List.of(shoeType)).image("image2").build());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDto> categoryDtos = categoryService.getAllCategories();

        Assertions.assertEquals(2, categoryDtos.size());
        Assertions.assertEquals(1L, categoryDtos.get(0).getId());
        Assertions.assertEquals("Category1", categoryDtos.get(0).getName());
        Assertions.assertEquals("image1", categoryDtos.get(0).getImage());
        Assertions.assertEquals(2L, categoryDtos.get(1).getId());
        Assertions.assertEquals("Category2", categoryDtos.get(1).getName());
        Assertions.assertEquals("image2", categoryDtos.get(1).getImage());
    }

    @Test
    void testGetCategoryShoeTypes() {
        long categoryId = 1L;
        List<ShoeType> shoeTypes = new ArrayList<>();
        shoeTypes.add(ShoeType.builder()
                .id(1L)
                .brand("Nike")
                .model("Air Max")
                .category(Category.builder().id(1L).build())
                .color("Black")
                .photos(List.of("photo1.jpg", "photo2.jpg"))
                .description("Comfortable running shoes")
                .price(100)
                .inStock(true)
                .products(List.of(
                        Product.builder().id(1L).build(),
                        Product.builder().id(2L).build(),
                        Product.builder().id(3L).build()
                ))
                .build());
        shoeTypes.add(ShoeType.builder()
                .id(2L)
                .brand("Adidas")
                .model("Superstar")
                .category(Category.builder().id(1L).build())
                .color("Black")
                .photos(List.of("photo1.jpg", "photo2.jpg"))
                .description("Comfortable running shoes")
                .price(100)
                .inStock(true)
                .products(List.of(
                        Product.builder().id(1L).build(),
                        Product.builder().id(2L).build(),
                        Product.builder().id(3L).build()
                ))
                .build());
        when(shoeTypeRepository.getShoeTypesByCategoryId(categoryId)).thenReturn(shoeTypes);

        List<ShoeTypeDto> shoeTypeDtos = categoryService.getCategoryShoeTypes(categoryId);

        Assertions.assertEquals(2, shoeTypeDtos.size());
        Assertions.assertEquals(1L, shoeTypeDtos.get(0).getId());
        Assertions.assertEquals("Nike", shoeTypeDtos.get(0).getBrand());
        Assertions.assertEquals("Air Max", shoeTypeDtos.get(0).getModel());
        Assertions.assertEquals(2L, shoeTypeDtos.get(1).getId());
        Assertions.assertEquals("Adidas", shoeTypeDtos.get(1).getBrand());
        Assertions.assertEquals("Superstar", shoeTypeDtos.get(1).getModel());
    }

    @Test
    void testGetCategoryShoeTypesLimited() {
        long categoryId = 1L;
        int colvo = 2;
        List<ShoeType> shoeTypes = new ArrayList<>();
        shoeTypes.add(ShoeType.builder()
                .id(1L)
                .brand("Nike")
                .model("Air Max")
                .category(Category.builder().id(1L).build())
                .color("Black")
                .photos(List.of("photo1.jpg", "photo2.jpg"))
                .description("Comfortable running shoes")
                .price(100)
                .inStock(true)
                .products(List.of(
                        Product.builder().id(1L).build(),
                        Product.builder().id(2L).build(),
                        Product.builder().id(3L).build()
                ))
                .build());
        shoeTypes.add(ShoeType.builder()
                .id(2L)
                .brand("Adidas")
                .model("Superstar")
                .category(Category.builder().id(1L).build())
                .color("Black")
                .photos(List.of("photo1.jpg", "photo2.jpg"))
                .description("Comfortable running shoes")
                .price(100)
                .inStock(true)
                .products(List.of(
                        Product.builder().id(1L).build(),
                        Product.builder().id(2L).build(),
                        Product.builder().id(3L).build()
                ))
                .build());
        shoeTypes.add(ShoeType.builder()
                .id(3L)
                .brand("Adidas")
                .model("Superstar")
                .category(Category.builder().id(1L).build())
                .color("Black")
                .photos(List.of("photo1.jpg", "photo2.jpg"))
                .description("Comfortable running shoes")
                .price(100)
                .inStock(true)
                .products(List.of(
                        Product.builder().id(1L).build(),
                        Product.builder().id(2L).build(),
                        Product.builder().id(3L).build()
                ))
                .build());
        when(shoeTypeRepository.getShoeTypesByCategoryIdLimited(categoryId, Limit.of(colvo))).thenReturn(shoeTypes.subList(0, colvo));

        List<ShoeTypeDto> shoeTypeDtos = categoryService.getCategoryShoeTypesLimited(categoryId, colvo);

        Assertions.assertEquals(2, shoeTypeDtos.size());
        Assertions.assertEquals(1L, shoeTypeDtos.get(0).getId());
        Assertions.assertEquals("Nike", shoeTypeDtos.get(0).getBrand());
        Assertions.assertEquals("Air Max", shoeTypeDtos.get(0).getModel());
        Assertions.assertEquals(2L, shoeTypeDtos.get(1).getId());
        Assertions.assertEquals("Adidas", shoeTypeDtos.get(1).getBrand());
        Assertions.assertEquals("Superstar", shoeTypeDtos.get(1).getModel());
    }

    @Test
    void testGetCategoryById() {
        long categoryId = 1L;
        Category category = Category.builder()
                .id(categoryId)
                .name("Category1")
                .products(List.of(ShoeType.builder().id(1).build()))
                .image("image1")
                .build();
        when(categoryRepository.getReferenceById(categoryId)).thenReturn(category);

        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);

        Assertions.assertEquals(category.getId(), categoryDto.getId());
        Assertions.assertEquals(category.getName(), categoryDto.getName());
        Assertions.assertEquals(category.getImage(), categoryDto.getImage());

    }

}