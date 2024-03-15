package org.lashop.newback.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.*;
import org.lashop.newback.repositories.AccountRepository;
import org.lashop.newback.repositories.CartRepository;
import org.lashop.newback.repositories.ProductRepository;
import org.lashop.newback.repositories.ShoeTypeRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShoeTypeRepository shoeTypeRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTakeProductsInCart() {
        long accountId = 1L;
        List<Cart> cartItems = Arrays.asList(Cart.builder().id(1L).shoeType(ShoeType.builder()
                .id(1)
                .brand("brand1")
                .model("model1")
                .category(Category.builder().id(1L).build())
                .color("black")
                .photos(List.of("photo1", "photo2"))
                .description("desc")
                .price(100)
                .inStock(true)
                .products(List.of(Product.builder().id(1).build()))
                .build()).quantity(2).size(40)
                .account(Account.builder().id(1L).build()).build());
        when(cartRepository.findAllByAccountId(accountId)).thenReturn(cartItems);

        List<ShoeTypeDto> result = cartService.takeProductsInCart(accountId);

        assertEquals(ShoeTypeDto.from(cartItems.stream().map(Cart::getShoeType).toList()), result);
    }

    @Test
    void testTakeCart() {
        long accountId = 1L;
        List<Cart> cartItems = Arrays.asList(Cart.builder().id(1L).shoeType(ShoeType.builder()
                        .id(1)
                        .brand("brand1")
                        .model("model1")
                        .category(Category.builder().id(1L).build())
                        .color("black")
                        .photos(List.of("photo1", "photo2"))
                        .description("desc")
                        .price(100)
                        .inStock(true)
                        .products(List.of(Product.builder().id(1).build()))
                        .build()).quantity(2).size(40)
                .account(Account.builder().id(1L).build()).build());

        when(cartRepository.findAllByAccountId(accountId)).thenReturn(cartItems);

        List<CartDto> res = cartService.takeCart(accountId);

        Assertions.assertEquals(CartDto.from(cartItems), res);
    }

    @Test
    void testMinusCount() {
        long shoeTypeId = 1L;
        double size = 42.0;
        long accountId = 1L;
        Cart cart = new Cart();
        cart.setQuantity(2);
        Optional<Cart> optionalCart = Optional.of(cart);
        when(cartRepository.findByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size)).thenReturn(optionalCart);
        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(shoeTypeId, size)).thenReturn(3);

        cartService.minusCount(shoeTypeId, size, accountId);

        assertEquals(1, cart.getQuantity());
    }

    @Test
    void testMinusCount2() {
        long shoeTypeId = 1L;
        double size = 42.0;
        long accountId = 1L;
        Cart cart = new Cart();
        cart.setQuantity(10);
        Optional<Cart> optionalCart = Optional.of(cart);
        when(cartRepository.findByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size)).thenReturn(optionalCart);
        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(shoeTypeId, size)).thenReturn(3);

        cartService.minusCount(shoeTypeId, size, accountId);

        assertEquals(3, cart.getQuantity());
    }

    @Test
    void testMinusCount3() {
        long shoeTypeId = 1L;
        double size = 42.0;
        long accountId = 1L;
        Cart cart = new Cart();
        cart.setQuantity(1);
        Optional<Cart> optionalCart = Optional.of(cart);
        when(cartRepository.findByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size)).thenReturn(optionalCart);
        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(shoeTypeId, size)).thenReturn(3);

        cartService.minusCount(shoeTypeId, size, accountId);

        verify(cartRepository, times(1)).deleteByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size);
    }

    @Test
    void testPlusCount() {
        long shoeTypeId = 1L;
        double size = 42.0;
        long accountId = 1L;
        Cart cart = new Cart();
        cart.setQuantity(1);
        Optional<Cart> optionalCart = Optional.of(cart);
        when(cartRepository.findByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size)).thenReturn(optionalCart);
        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(shoeTypeId, size)).thenReturn(3);

        cartService.plusCount(shoeTypeId, size, accountId);

        verify(cartRepository, times(1)).save(cart);
        assertEquals(2, cart.getQuantity());
    }

    @Test
    void testPlusCount2() {
        long shoeTypeId = 1L;
        double size = 42.0;
        long accountId = 1L;
        Cart cart = new Cart();
        cart.setQuantity(4);
        Optional<Cart> optionalCart = Optional.of(cart);
        when(cartRepository.findByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size)).thenReturn(optionalCart);
        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(shoeTypeId, size)).thenReturn(3);

        cartService.plusCount(shoeTypeId, size, accountId);

        verify(cartRepository, times(1)).save(cart);
        assertEquals(3, cart.getQuantity());
    }

    @Test
    void testAddToCart() {
        // Arrange
        long shoeTypeId = 1L;
        double size = 42.0;
        long accountId = 1L;
        Cart cart = new Cart();
        cart.setQuantity(0);
        Optional<Cart> optionalCart = Optional.of(cart);
        when(cartRepository.findByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size)).thenReturn(optionalCart);
        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(shoeTypeId, size)).thenReturn(3);

        cartService.addToCart(shoeTypeId, size, accountId);

        verify(cartRepository, times(1)).save(cart);
        assertEquals(1, cart.getQuantity());
    }

    @Test
    void testAddToCart2() {
        // Arrange
        long shoeTypeId = 1L;
        double size = 42.0;
        long accountId = 1L;
//        Optional<Cart> optionalCart = Optional.of(cart);
        when(cartRepository.findByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size)).thenReturn(Optional.empty());
        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(shoeTypeId, size)).thenReturn(3);
        when(shoeTypeRepository.findById(shoeTypeId)).thenReturn(Optional.of(ShoeType.builder().id(shoeTypeId).build()));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(Account.builder().id(accountId).build()));

        cartService.addToCart(shoeTypeId, size, accountId);

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddToCart3() {
        // Arrange
        long shoeTypeId = 1L;
        double size = 42.0;
        long accountId = 1L;
//        Optional<Cart> optionalCart = Optional.of(cart);
        when(cartRepository.findByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size)).thenReturn(Optional.empty());
        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(shoeTypeId, size)).thenReturn(0);
        when(shoeTypeRepository.findById(shoeTypeId)).thenReturn(Optional.of(ShoeType.builder().id(shoeTypeId).build()));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(Account.builder().id(accountId).build()));

        Assertions.assertThrows(RuntimeException.class, () -> {
            cartService.addToCart(shoeTypeId, size, accountId);
        });

//        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testCheckStockOfProductsCart() {
        // Arrange
        long accountId = 1L;
        List<Cart> cartList = new ArrayList<>();
        cartList.add(createCart(1L, 42.0, 3)); // Assuming there are 3 products of this type available
        cartList.add(createCart(2L, 39.5, 2)); // Assuming there are 2 products of this type available
        when(cartRepository.findAllByAccountId(accountId)).thenReturn(cartList);
        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(any(Long.class), anyDouble())).thenReturn(5);

        // Act
        cartService.checkStockOfProductsCart(accountId);

        // Assert
        verify(cartRepository, times(0)).deleteByShoeTypeIdAndAccountIdAndSize(2L, accountId, 39.5); // The second item should be deleted
        verify(cartRepository, times(0)).deleteByShoeTypeIdAndAccountIdAndSize(1L, accountId, 42.0); // The first item should not be deleted
        verify(cartRepository, times(0)).save(any(Cart.class)); // One item's quantity should be updated
    }

    // Helper method to create a Cart object
    private Cart createCart(long shoeTypeId, double size, int quantity) {
        Cart cart = new Cart();
        cart.setShoeType(new ShoeType());
        cart.getShoeType().setId(shoeTypeId);
        cart.setSize(size);
        cart.setQuantity(quantity);
        return cart;
    }

    @Test
    void testCheckStockOfProductsCart2() {
        // Arrange
        long accountId = 1L;
        List<Cart> cartList = new ArrayList<>();
        cartList.add(createCart(1L, 42.0, 3)); // Assuming there are 3 products of this type available
        cartList.add(createCart(2L, 39.5, 3)); // Assuming there are 2 products of this type available
        when(cartRepository.findAllByAccountId(accountId)).thenReturn(cartList);
        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(any(Long.class), anyDouble())).thenReturn(2);

        // Act
        cartService.checkStockOfProductsCart(accountId);

        // Assert
        verify(cartRepository, times(0)).deleteByShoeTypeIdAndAccountIdAndSize(2L, accountId, 39.5); // The second item should be deleted
        verify(cartRepository, times(0)).deleteByShoeTypeIdAndAccountIdAndSize(1L, accountId, 42.0); // The first item should not be deleted
        verify(cartRepository, times(2)).save(any(Cart.class)); // One item's quantity should be updated
    }

    @Test
    void testCheckStockOfProductsCart3() {
        // Arrange
        long accountId = 1L;
        List<Cart> cartList = new ArrayList<>();
        cartList.add(createCart(1L, 42.0, 3)); // Assuming there are 3 products of this type available
        cartList.add(createCart(2L, 39.5, 3)); // Assuming there are 2 products of this type available
        when(cartRepository.findAllByAccountId(accountId)).thenReturn(cartList);
        when(productRepository.countProductsByShoeTypeIdAndSizeNotSold(any(Long.class), anyDouble())).thenReturn(0);

        // Act
        cartService.checkStockOfProductsCart(accountId);

        // Assert
        verify(cartRepository, times(1)).deleteByShoeTypeIdAndAccountIdAndSize(2L, accountId, 39.5); // The second item should be deleted
        verify(cartRepository, times(1)).deleteByShoeTypeIdAndAccountIdAndSize(1L, accountId, 42.0); // The first item should not be deleted
        verify(cartRepository, times(0)).save(any(Cart.class)); // One item's quantity should be updated
    }

    // Similarly, write tests for other methods like plusCount, deleteItemFromCart, addToCart, checkStockOfProductsCart
}