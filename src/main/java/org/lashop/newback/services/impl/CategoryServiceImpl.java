package org.lashop.newback.services.impl;

import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ShoeTypeRepository shoeTypeRepository;

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
//        Pageable limit = PageRequest.of(0, colvo);
        Limit limit = Limit.of(colvo);
        return ShoeTypeDto.from(shoeTypeRepository.getShoeTypesByCategoryIdLimited(categoryId, limit));
    }

    @Override
    public CategoryDto getCategoryById(long categoryId) {
        try {
            Category category = categoryRepository.getReferenceById(categoryId);
            return CategoryDto.from(category);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Category not found", e);
        }
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
    public void createCategory(String categoryName, String image) {
        Optional<Category> categoryFind = categoryRepository.findCategoryByName(categoryName);

        if (categoryFind.isPresent()) {
            throw new RuntimeException("not unique name");
        }

        Category category = Category.builder()
                .name(categoryName)
                .image(image)
                .build();
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void deleteCategoryAndTakeTypesNotInStock(String categoryName) {
        Optional<Category> category = categoryRepository.findCategoryByName(categoryName);

        if (category.isPresent()) {
            Category categoryPres = category.get();

            // меняем поле inStock на false
            shoeTypeRepository.makeAllNotInStockByCategoryId(categoryPres.getId());

            // удаление категории и зануление ссылок в таблице с типами
            categoryRepository.updateAllByCategoryIdToNull(categoryPres.getId());
            categoryRepository.deleteById(categoryPres.getId());
        }
    }
}
