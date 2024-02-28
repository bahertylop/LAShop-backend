package org.lashop.newback.services;

import org.lashop.newback.dto.CartDto;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.ShoeType;
import org.lashop.newback.repositories.CartRepository;

import java.util.List;

public interface CartService {

    List<ShoeTypeDto> takeProductsInCart(long accountId);

    List<CartDto> takeCart(long accountId);

    void minusCount(long shoeTypeId, double size, long accountId);
    void plusCount(long shoeTypeId, double size, long accountId);

    void deleteItemFromCart(long shoeTypeId, double size, long accountId);

    void addToCart(long shoeTypeId, double size, long accountId);

    void checkStockOfProductsCart(long accountId);
}
