package org.lashop.newback.services.impl;

import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.CategoryDto;
import org.lashop.newback.dto.ProductDto;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.Category;
import org.lashop.newback.models.Product;
import org.lashop.newback.models.ShoeType;
import org.lashop.newback.repositories.CategoryRepository;
import org.lashop.newback.repositories.ProductRepository;
import org.lashop.newback.repositories.ShoeTypeRepository;
import org.lashop.newback.services.CategoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ShoeTypeRepository shoeTypeRepository;
    private final ProductRepository productRepository;

    @Override
    public List<CategoryDto> getAllCategories() {
        return CategoryDto.from(categoryRepository.findAll());
    }

    @Override
    public List<ShoeTypeDto> getCategoryShoeTypes(long categoryId) {
        return ShoeTypeDto.from(shoeTypeRepository.getShoeTypesByCategoryId(categoryId));
    }

    @Override
    public List<ShoeTypeDto> getCategoryShoeTypesLimited(long categoryId, int colvo) {
        Pageable limit = (Pageable) PageRequest.of(0, colvo);
        return ShoeTypeDto.from(shoeTypeRepository.getShoeTypesByCategoryIdLimited(categoryId, limit));
    }

    @Override
    public CategoryDto getCategoryById(long categoryId) {
        return CategoryDto.from(categoryRepository.getReferenceById(categoryId));
    }

    @Override
    public CategoryDto getShoeTypeCategory(ShoeTypeDto shoeTypeDto) {
        return CategoryDto.from(categoryRepository.getReferenceById(shoeTypeDto.getCategoryId()));
    }

    @Override
    public CategoryDto getProductCategory(ProductDto productDto) {
        ShoeType shoeType = shoeTypeRepository.getReferenceById(productDto.getTypeId());
        return CategoryDto.from(categoryRepository.getReferenceById(shoeType.getCategory().getId()));
    }

    @Override
    public void createCategory(String categoryName) {
        Category category = Category.builder()
                .name(categoryName)
                .build();
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
