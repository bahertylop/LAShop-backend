package org.lashop.newback.services.impl;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.ShoeType;
import org.lashop.newback.repositories.CategoryRepository;
import org.lashop.newback.repositories.ProductRepository;
import org.lashop.newback.repositories.ShoeTypeRepository;
import org.lashop.newback.services.ShoeTypeService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.management.RuntimeMBeanException;
import java.util.List;
import java.util.Optional;


// посмотреть что да как вообще с обработкой исключений происходит
@Service
@RequiredArgsConstructor
public class ShoeTypeServiceImpl implements ShoeTypeService {

    private final ShoeTypeRepository shoeTypeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ShoeTypeDto> getTypesList() {
        List<ShoeType> shoeTypes = shoeTypeRepository.findAll();
        return ShoeTypeDto.from(shoeTypes);
    }

    @Override
    public ShoeTypeDto getTypeById(long id) {
        Optional<ShoeType> shoeType = shoeTypeRepository.findById(id);
        return shoeType.map(ShoeTypeDto::from).orElseThrow(() -> new RuntimeException("shoeType not found"));
    }

    @Override
    public void deleteShoeType(long id) {
        shoeTypeRepository.deleteById(id);
    }

    // посмотреть как будут передавать фотки
    @Override
    public void createNewShoeType(ShoeTypeDto shoeTypeDto) {
        ShoeType shoeType = ShoeType.builder()
                .brand(shoeTypeDto.getBrand())
                .model(shoeTypeDto.getModel())
                .price(shoeTypeDto.getPrice())
                .color(shoeTypeDto.getColor())
                .photos(shoeTypeDto.getPhotos())
                .description(shoeTypeDto.getDescription())
                .inStock(false)
                .category(categoryRepository.findById(shoeTypeDto.getCategoryId()).orElseThrow(() -> new RuntimeException("category not found")))
                .build();

        shoeTypeRepository.save(shoeType);
    }

    @Override
    public void updateShoeType(ShoeTypeDto shoeTypeDto) {
        Optional<ShoeType> shoeType = shoeTypeRepository.findById(shoeTypeDto.getId());

        if (shoeType.isPresent()) {
            ShoeType shoeTypeReal = shoeType.get();
            shoeTypeReal.setBrand(shoeTypeDto.getBrand());
            shoeTypeReal.setModel(shoeTypeDto.getModel());
            shoeTypeReal.setPrice(shoeTypeDto.getPrice());
            shoeTypeReal.setColor(shoeTypeDto.getColor());
            shoeTypeReal.setPhotos(shoeTypeDto.getPhotos());
            shoeTypeReal.setDescription(shoeTypeDto.getDescription());
            shoeTypeReal.setInStock(false);
            shoeTypeReal.setCategory(categoryRepository.findById(shoeTypeDto.getCategoryId()).orElseThrow(() -> new RuntimeException("category not found")));
            shoeTypeRepository.save(shoeTypeReal);
        }
    }

    @Override
    public List<ShoeTypeDto> getShoeTypesIsInStock(boolean isInStock) {
        return ShoeTypeDto.from(shoeTypeRepository.getShoeTypesByInStock(isInStock));
    }

    @Override
    public void changeInStock(Long shoeTypeId, boolean inStock) {
        Optional<ShoeType> shoeType = shoeTypeRepository.findById(shoeTypeId);

        if (shoeType.isPresent()) {
            shoeTypeRepository.changeInStockById(shoeTypeId, inStock);
        } else {
            throw new RuntimeException("shoe type not found");
        }
    }
}
