package org.lashop.newback.repositories;

import jakarta.transaction.Transactional;
import org.lashop.newback.models.Cart;
import org.lashop.newback.models.ShoeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByAccountId(long accountId);

    Optional<Cart> findByShoeTypeIdAndAccountIdAndSize(long shoeTypeId, long accountId, double size);

    @Transactional
    void deleteByShoeTypeIdAndAccountIdAndSize(long shoeTypeId, long accountId, double size);

    @Transactional
    void deleteByAccountId(long account_id);

//    @Transactional
//    default void plusQuantity(long shoeType, double size, long accountId) {
//        Optional<Cart> position = findByShoeTypeIdAndAccountIdAndSize(shoeType, accountId, size);
//
//        if (position.isPresent()) {
//            Cart cart = position.get();
//
//
//        }
//    }
}
