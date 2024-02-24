package org.lashop.newback.repositories;

import jakarta.transaction.Transactional;
import org.lashop.newback.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findCategoryByName(String categoryName);

    @Modifying
    @Transactional
    @Query("UPDATE ShoeType p SET p.category = null WHERE p.category.id = ?1")
    void updateAllByCategoryIdToNull(long categoryId);
}
