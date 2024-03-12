package org.lashop.newback.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lashop.newback.models.Category;
import org.lashop.newback.models.ShoeType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoTest {

    @Test
    void testFromForOneObject() {
        CategoryDto categoryDtoRes = CategoryDto.builder()
                .id(1)
                .name("Running Shoes")
                .image("running_shoes.jpg")
                .shoeTypeIds(List.of(1L, 2L, 3L))
                .build();

        Category category = Category.builder()
                .id(1)
                .name("Running Shoes")
                .image("running_shoes.jpg")
                .products(List.of(
                        ShoeType.builder().id(1).build(),
                        ShoeType.builder().id(2).build(),
                        ShoeType.builder().id(3).build()
                ))
                .build();

        CategoryDto categoryDtoMy = CategoryDto.from(category);
        Assertions.assertEquals(categoryDtoRes, categoryDtoMy);
    }

    @Test
    void testFromForListOfObjects() {
        List<Category> categoryList = List.of(
                Category.builder()
                        .id(1)
                        .name("Running Shoes")
                        .image("running_shoes.jpg")
                        .products(List.of(
                                ShoeType.builder().id(1).build(),
                                ShoeType.builder().id(2).build(),
                                ShoeType.builder().id(3).build()
                        ))
                        .build(),
                Category.builder()
                        .id(2)
                        .name("Casual Shoes")
                        .image("casual_shoes.jpg")
                        .products(List.of(
                                ShoeType.builder().id(4).build(),
                                ShoeType.builder().id(5).build()
                        ))
                        .build()
        );

        List<CategoryDto> categoryDtoList = CategoryDto.from(categoryList);

        List<CategoryDto> expectedCategoryDtoList = List.of(
                CategoryDto.builder()
                        .id(1)
                        .name("Running Shoes")
                        .image("running_shoes.jpg")
                        .shoeTypeIds(List.of(1L, 2L, 3L))
                        .build(),
                CategoryDto.builder()
                        .id(2)
                        .name("Casual Shoes")
                        .image("casual_shoes.jpg")
                        .shoeTypeIds(List.of(4L, 5L))
                        .build()
        );

        Assertions.assertEquals(expectedCategoryDtoList, categoryDtoList);
    }
}