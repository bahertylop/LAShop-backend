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
        return shoeType.map(ShoeTypeDto::from).orElse(null);
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
                .inStock(shoeTypeDto.isInStock())
                .category(categoryRepository.getReferenceById(shoeTypeDto.getCategoryId()))
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
            shoeTypeReal.setInStock(shoeTypeDto.isInStock());
            shoeTypeReal.setCategory(categoryRepository.getReferenceById(shoeTypeDto.getCategoryId()));
            shoeTypeRepository.save(shoeTypeReal);
        }
    }
}
