package org.lashop.newback.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lashop.newback.models.Product;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    private Long typeId;
    private Double size;
    private Boolean sold;

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .typeId(product.getShoeType().getId())
                .size(product.getSize())
                .sold(product.isSold())
                .build();
    }

    public static List<ProductDto> from(List<Product> productList) {
        return productList.stream().map(ProductDto::from).toList();
    }
}
