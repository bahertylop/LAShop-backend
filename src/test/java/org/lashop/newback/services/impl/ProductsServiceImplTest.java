package org.lashop.newback.services.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lashop.newback.dto.ProductDto;
import org.lashop.newback.dto.requests.SizeQuantity;
import org.lashop.newback.models.Product;
import org.lashop.newback.models.ShoeType;
import org.lashop.newback.repositories.ProductRepository;
import org.lashop.newback.repositories.ShoeTypeRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductsServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShoeTypeRepository shoeTypeRepository;

    @InjectMocks
    private ProductsServiceImpl productsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        List<Product> productList = List.of(
                Product.builder()
                        .id(1L)
                        .shoeType(ShoeType.builder().id(1).build())
                        .sold(false)
                        .size(40)
                        .build(),
                Product.builder()
                        .id(2L)
                        .shoeType(ShoeType.builder().id(1).build())
                        .sold(false)
                        .size(41)
                        .build()
                );

        when(productRepository.findAll()).thenReturn(productList);

        List<ProductDto> result = productsService.getAllProducts();

        assertEquals(ProductDto.from(productList), result);
    }

    @Test
    void testGetAllSizesForType() {
        Long typeId = 1L;

        List<Double> sizes = List.of(40.0, 42.5, 43.0, 44.0);

        when(productRepository.findSizesByShoeTypeId(typeId)).thenReturn(sizes);

        List<Double> res = productsService.getAllSizesForType(typeId);

        Assertions.assertEquals(sizes, res);
    }

    @Test
    void testGetSizesQuantity() {
        Long typeId = 5L;
        Map<Double, Integer> sizesQuantity = Map.of(40.0, 5, 41.0, 3, 42.5, 5);

        List<Object[]> sizesQuan = new ArrayList<>();
        List<Double> sizes = sizesQuantity.keySet().stream().toList();
        for (int i = 0; i < sizes.size(); i++) {
            Object[] objects = new Object[2];
            double size = sizes.get(i);
            int quantity = sizesQuantity.get(size);
            objects[0] = size;
            objects[1] = quantity;
            sizesQuan.add(objects);
        }

        when(productRepository.findSizesAndCountsByShoeTypeIdAndNotSold(typeId)).thenReturn(sizesQuan);

        Map<Double, Integer> res = productsService.getSizesQuantity(typeId);

        Assertions.assertEquals(sizesQuantity, res);
    }

    @Test
    void testGetQuantityInStock() {
        int quantity = 5;
        long typeId = 10L;

        when(productRepository.countProductsByShoeTypeIdNotSold(typeId)).thenReturn(quantity);

        int res = productsService.getQuantityInStock(typeId);

        Assertions.assertEquals(quantity, res);
    }

    @Test
    void testGetQuantityInStockBySize() {
        int quantity = 5;
        long typeId = 10L;
        double size = 40.5;

        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(typeId, size)).thenReturn(quantity);

        int res = productsService.getQuantityInStockBySize(typeId, size);

        Assertions.assertEquals(quantity, res);
    }

    @Test
    void testDeleteProductBySize() {
        long id = 10L;

        productsService.deleteProductById(id);

        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteProductByTypeId() {
        long typeId = 10L;

        productsService.deleteProductByTypeId(typeId);

        verify(productRepository, times(1)).deleteAllByShoeTypeId(typeId);
    }

    @Test
    void testSellProducts() {
        long typeId = 1L;
        double size = 40.0;
        int quantity = 3;

        Pageable prodLimit = PageRequest.of(0, quantity);

        List<Product> products = List.of(
                Product.builder().id(1).sold(false).size(40).shoeType(ShoeType.builder().id(11).build()).build(),
                Product.builder().id(2).sold(false).size(40).shoeType(ShoeType.builder().id(11).build()).build(),
                Product.builder().id(3).sold(false).size(40).shoeType(ShoeType.builder().id(11).build()).build()
        );

        when(productRepository.findTopNByShoeTypeIdAndSizeAndSoldFalseOrderById(typeId, size, prodLimit)).thenReturn(products);

        List<Long> ids = productsService.sellProducts(typeId, size, quantity);

        verify(productRepository, times(1)).findTopNByShoeTypeIdAndSizeAndSoldFalseOrderById(typeId, size, prodLimit);
        verify(productRepository, times(3)).updateSoldStatusById(any(Long.class));
        Assertions.assertEquals(products.stream().map(Product::getId).toList(), ids);
    }

    @Test
    void testAddNewProduct() {
        long typeId = 1L;
        double size = 40.0;
        ProductDto productDto = ProductDto.builder().typeId(typeId).size(size).build();

        when(shoeTypeRepository.findById(typeId)).thenReturn(Optional.of(ShoeType.builder().id(1L).build()));

        productsService.addNewProduct(productDto);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testAddNewProductThrowException1() {
        long typeId = 1L;
        double size = 40.0;
        ProductDto productDto = ProductDto.builder().typeId(typeId).size(size).build();

        when(shoeTypeRepository.findById(typeId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> productsService.addNewProduct(productDto));
    }

    @Test
    void testAddNewProductThrowException2() {
        Long typeId = null;
        Double size = null;
        ProductDto productDto = ProductDto.builder().typeId(typeId).size(size).build();

        when(shoeTypeRepository.findById(typeId)).thenReturn(Optional.of(ShoeType.builder().id(1L).build()));

        Assertions.assertThrows(RuntimeException.class, () -> productsService.addNewProduct(productDto));
    }

    @Test
    void testAddNewProduct_WithTypeIdAndSize_Success() {
        long typeId = 1L;
        double size = 42.0;

        when(shoeTypeRepository.findById(anyLong())).thenReturn(Optional.of(new ShoeType()));

        productsService.addNewProduct(typeId, size);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testAddNewProduct_WithTypeIdAndSize_ShoeTypeNotFound_ThrowsException() {
        long typeId = 1L;
        double size = 42.0;

        when(shoeTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productsService.addNewProduct(typeId, size));
    }

    @Test
    void testAddSomeNewProducts_WithProductDtoAndQuantity_Success() {
        ProductDto productDto = new ProductDto();
        productDto.setTypeId(1L);
        productDto.setSize(42.0);
        int quantity = 3;

        when(shoeTypeRepository.findById(anyLong())).thenReturn(Optional.of(new ShoeType()));

        productsService.addSomeNewProducts(productDto, quantity);

        verify(productRepository, times(quantity)).save(any(Product.class));
    }

    @Test
    void testAddSomeNewProducts_WithTypeIdAndSizeAndQuantity_Success() {
        long typeId = 1L;
        double size = 42.0;
        int quantity = 3;

        when(shoeTypeRepository.findById(anyLong())).thenReturn(Optional.of(new ShoeType()));

        productsService.addSomeNewProducts(typeId, size, quantity);

        verify(productRepository, times(quantity)).save(any(Product.class));
    }
}