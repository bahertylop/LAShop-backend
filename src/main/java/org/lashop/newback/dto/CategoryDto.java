package org.lashop.newback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lashop.newback.models.Category;
import org.lashop.newback.models.ShoeType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private long id;
    private String name;
    private List<Long> productIds;

    public static CategoryDto from(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .productIds(category.getProducts().stream().map(ShoeType::getId).toList())
                .build();
    }

    public static List<CategoryDto> from(List<Category> categories) {
        return categories.stream().map(CategoryDto::from).toList();
    }
}

