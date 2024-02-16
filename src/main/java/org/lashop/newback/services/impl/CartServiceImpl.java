package org.lashop.newback.services.impl;

import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.Cart;
import org.lashop.newback.models.ShoeType;
import org.lashop.newback.repositories.AccountRepository;
import org.lashop.newback.repositories.CartRepository;
import org.lashop.newback.repositories.ProductRepository;
import org.lashop.newback.repositories.ShoeTypeRepository;
import org.lashop.newback.services.CartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public List<ShoeTypeDto> takeProductsInCart(long accountId) {
        return cartRepository.findAllByAccountId(accountId)
                .stream()
                .map(Cart::getShoeType)
                .map(ShoeTypeDto::from)
                .toList();
    }

    @Override
    public List<CartDto> takeCart(long accountId) {
        return cartRepository.findAllByAccountId(accountId)
                .stream()
                .map(CartDto::from)
                .toList();
    }


    private final ProductRepository productRepository;
    // если что можно переделать чтобы при нажатии на минус при количестве 1 удалялся продукт из корзины
    @Override
    public void minusCount(long shoeTypeId, double size, long accountId) {
        Optional<Cart> position = cartRepository.findByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size);

        if (position.isPresent()) {
            Cart cart = position.get();

            int countProducts = productRepository.countProductsByShoeTypeIdAndSizeNotSold(shoeTypeId, size);
            int quantity = cart.getQuantity();

            if (quantity > countProducts) {
                cart.setQuantity(countProducts);
            } else if (quantity > 1) {
                cart.setQuantity(quantity - 1);
            }

            cartRepository.save(cart);
        }
    }

    @Override
    public void plusCount(long shoeTypeId, double size, long accountId) {
        Optional<Cart> position = cartRepository.findByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size);

        if (position.isPresent()) {
            Cart cart = position.get();

            int countProducts = productRepository.countProductsByShoeTypeIdAndSizeNotSold(shoeTypeId, size);

            if (cart.getQuantity() < countProducts) {
                cart.setQuantity(cart.getQuantity() + 1);
            } else {
                cart.setQuantity(countProducts);
            }
            cartRepository.save(cart);
        }
    }

    @Override
    public void deleteItemFromCart(long shoeTypeId, double size, long accountId) {
        cartRepository.deleteByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size);
    }

    private final ShoeTypeRepository shoeTypeRepository;
    private final AccountRepository accountRepository;
    @Override
    public void addToCart(long shoeTypeId, double size, long accountId) {
        Optional<Cart> position = cartRepository.findByShoeTypeIdAndAccountIdAndSize(shoeTypeId, accountId, size);

        if (position.isEmpty()) {
            // добавляем
            int countPairs = productRepository.countProductsByShoeTypeIdAndSizeNotSold(shoeTypeId, size);
            if (countPairs > 0) {
                Cart newItemInCart = Cart.builder()
                        .shoeType(shoeTypeRepository.findById(shoeTypeId).orElseThrow(() -> new RuntimeException("shoeType not found")))
                        .account(accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("account not found")))
                        .size(size)
                        .quantity(1)
                        .build();
                cartRepository.save(newItemInCart);
            } else {
                throw new RuntimeException("no position in stock");
            }

        } else {
            // прибавляем
            plusCount(shoeTypeId, size, accountId);
        }
    }
}
