package org.lashop.newback.repositories;

import org.lashop.newback.models.ShoeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShoeTypeRepository extends JpaRepository<ShoeType, Long> {

    List<ShoeType> getShoeTypesByCategoryId(long categoryId);

    @Query("SELECT p from ShoeType p where p.category.id = ?1")
    List<ShoeType> getShoeTypesByCategoryIdLimited(long categoryId, Pageable limit);
}
