package org.lashop.newback.services;

import org.lashop.newback.dto.ProductDto;
import org.lashop.newback.models.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    // все продукты
    List<ProductDto> getAllProducts();

    // все размеры для определенной модели
    List<Double> getAllSizesForType(long typeId);

    // все доступные размеры с с количеством
    Map<Double, Integer> getSizesQuantity(long typeId);

    // общее количество пар в наличии
    int getQuantityInStock(long typeId);

    // количество пар определенного размера
    int getQuantityInStockBySize(long typeId, double size);

    void deleteProductById(long id);

    void deleteProductByTypeId(long typeId);

    // тут тип надо в
    List<Long> sellProducts(long typeId, double size, int quantity);

    void addNewProduct(ProductDto productDto);

    void addNewProduct(long typeId, double size, boolean sold);

    void addSomeNewProducts(ProductDto productDto, int quantity);

    void addSomeNewProduct(long typeId, double size, boolean sold, int quantity);
}
