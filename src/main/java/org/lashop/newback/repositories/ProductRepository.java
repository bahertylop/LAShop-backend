package org.lashop.newback.repositories;

import jakarta.transaction.Transactional;
import org.lashop.newback.models.Product;
import org.lashop.newback.models.ShoeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p.size from Product p where p.shoeType.id = :shoeTypeId and p.sold = false")
    List<Double> findSizesByShoeTypeId(long shoeTypeId);

    @Query("SELECT p.size, COUNT(p) FROM Product p WHERE p.shoeType.id = :shoeTypeId AND p.sold = false GROUP BY p.size")
    List<Object[]> findSizesAndCountsByShoeTypeIdAndNotSold(long shoeTypeId);

    @Query("select count(p) from Product p where p.shoeType.id = :shoeTypeId and p.sold = false")
    int countProductsByShoeTypeIdNotSold(long shoeTypeId);

    @Query("select count(p) from Product p where p.shoeType.id = :shoeTypeId and p.size = :size and p.sold = false")
    int countProductsByShoeTypeIdAndSizeNotSold(long shoeTypeId, double size);

    void deleteAllByShoeTypeId(long shoeTypeId);

    List<Product> findTopNByShoeTypeIdAndSizeAndSoldFalseOrderById(long shoeTypeId, double size, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.sold = true WHERE p.id = :id and p.sold = false")
    void updateSoldStatusById(long id);

}