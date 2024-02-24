package org.lashop.newback.services;

import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.ShoeType;

import java.util.List;

public interface ShoeTypeService {

    List<ShoeTypeDto> getTypesList();

    ShoeTypeDto getTypeById(long id);

    void deleteShoeType(long id);

    void createNewShoeType(ShoeTypeDto shoeTypeDto);

    void updateShoeType(ShoeTypeDto shoeTypeDto);

    List<ShoeTypeDto> getShoeTypesIsInStock(boolean isInStock);
}
