package org.lashop.newback.services;

import org.lashop.newback.dto.CategoryDto;
import org.lashop.newback.dto.ProductDto;
import org.lashop.newback.dto.ShoeTypeDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategories();

    List<ShoeTypeDto> getCategoryShoeTypes(long categoryId);

    List<ShoeTypeDto> getCategoryShoeTypesLimited(long categoryId, int limit);

    CategoryDto getCategoryById(long categoryId);

    CategoryDto getShoeTypeCategory(ShoeTypeDto shoeTypeDto);

    CategoryDto getProductCategory(ProductDto productDto);

    void createCategory(String categoryName);

    void deleteCategory(long id);

}
