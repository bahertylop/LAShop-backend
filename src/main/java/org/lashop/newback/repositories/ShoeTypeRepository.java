package org.lashop.newback.repositories;

import org.lashop.newback.models.ShoeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface ShoeTypeRepository extends JpaRepository<ShoeType, Long> {

    List<ShoeType> getShoeTypesByCategoryId(long categoryId);

    @Query("SELECT p from ShoeType p where p.category.id = :categoryId")
    List<ShoeType> getShoeTypesByCategoryIdLimited(long categoryId, Pageable limit);
}
