package org.lashop.newback.repositories;

import jakarta.transaction.Transactional;
import org.lashop.newback.models.Favourite;
import org.lashop.newback.models.ShoeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavouritesRepository extends JpaRepository<Favourite, Long> {

    List<Favourite> findAllByAccountId(long accountId);

    @Transactional
    void deleteAllByAccountId(long accountId);

    Optional<Favourite> findByAccountIdAndShoeTypeId(long userId, long shoeTypeId);

    @Transactional
    void deleteByAccountIdAndShoeTypeId(long userId, long shoeTypeId);
}
