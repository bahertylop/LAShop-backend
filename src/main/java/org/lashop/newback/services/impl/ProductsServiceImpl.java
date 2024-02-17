package org.lashop.newback.services.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.ProductDto;
import org.lashop.newback.models.Product;
import org.lashop.newback.repositories.ProductRepository;
import org.lashop.newback.repositories.ShoeTypeRepository;
import org.lashop.newback.services.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return ProductDto.from(productList);
    }

    @Override
    public List<Double> getAllSizesForType(long typeId) {
        return productRepository.findSizesByShoeTypeId(typeId);
    }

    @Override
    public Map<Double, Integer> getSizesQuantity(long typeId) {
        List<Object[]> sizesQuantity = productRepository.findSizesAndCountsByShoeTypeIdAndNotSold(typeId);

        Map<Double, Integer> mapSizesQuantity = new HashMap<>();
        for (int i = 0; i < sizesQuantity.size(); i++) {
            Object[] qurSizesQuantity = sizesQuantity.get(i);
            Double size = (Double) qurSizesQuantity[0];
            Integer quantity = (Integer) qurSizesQuantity[1];
            mapSizesQuantity.put(size, quantity);
        }

        return mapSizesQuantity;
    }

    @Override
    public int getQuantityInStock(long typeId) {
        return productRepository.countProductsByShoeTypeIdNotSold(typeId);
    }

    @Override
    public int getQuantityInStockBySize(long typeId, double size) {
        return productRepository.countProductsByShoeTypeIdAndSizeNotSold(typeId, size);
    }

    @Override
    public void deleteProductById(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void deleteProductByTypeId(long typeId) {
        productRepository.deleteAllByShoeTypeId(typeId);
    }

    @Override
    public List<Long> sellProducts(long typeId, double size, int quantity) {

        Pageable prodLimit = PageRequest.of(0, quantity);
        List<Product> productForSale = productRepository.findTopNByShoeTypeIdAndSizeAndSoldFalseOrderById(typeId, size, prodLimit);

        for (Product product : productForSale) {
            productRepository.updateSoldStatusById(product.getId());
        }

        return productForSale.stream().map(Product::getId).toList();
    }


    // посмотреть как будет работать
    private final ShoeTypeRepository shoeTypeRepository;
    @Override
    public void addNewProduct(ProductDto productDto) {
        Product product = Product.builder()
                .shoeType(shoeTypeRepository.findById(productDto.getTypeId()).orElse(null))
                .size(productDto.getSize())
                .sold(productDto.isSold())
                .build();

        productRepository.save(product);
    }

    @Override
    public void addNewProduct(long typeId, double size, boolean sold) {
        Product product = Product.builder()
                .shoeType(shoeTypeRepository.findById(typeId).orElse(null))
                .size(size)
                .sold(sold)
                .build();

        productRepository.save(product);
    }

    @Override
    public void addSomeNewProducts(ProductDto productDto, int quantity) {
        for (int i = 0; i < quantity; i++) {
            addNewProduct(productDto);
        }
    }

    @Override
    public void addSomeNewProduct(long typeId, double size, boolean sold, int quantity) {
        for (int i = 0; i < quantity; i++) {
            addNewProduct(typeId, size, sold);
        }
    }
}
