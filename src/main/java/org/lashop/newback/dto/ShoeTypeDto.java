package org.lashop.newback.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lashop.newback.models.Category;
import org.lashop.newback.models.Product;
import org.lashop.newback.models.ShoeType;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoeTypeDto {

    private long id;
//    private String brand;
//    private String model;
    private String name;
    private long categoryId;
    private String color;
    private List<String> photos;
    private String description;
    private int price;
    private boolean inStock;
    private List<Long> productsId;

    public static ShoeTypeDto from(ShoeType shoeType) {
        return ShoeTypeDto.builder()
                .id(shoeType.getId())
                .name(shoeType.getBrand() + " " + shoeType.getModel())
                .categoryId(shoeType.getCategory().getId())
                .color(shoeType.getColor())
                .photos(shoeType.getPhotos())
                .description(shoeType.getDescription())
                .price(shoeType.getPrice())
                .inStock(shoeType.isInStock())
                .productsId(shoeType.getProducts().stream().map(Product::getId).toList())
                .build();
    }

    public static List<ShoeTypeDto> from(List<ShoeType> shoeTypes) {
        return shoeTypes.stream().map(ShoeTypeDto::from).toList();
    }
}
