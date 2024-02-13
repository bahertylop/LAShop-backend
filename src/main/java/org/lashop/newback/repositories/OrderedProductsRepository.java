package org.lashop.newback.repositories;

import org.lashop.newback.models.OrderedProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductsRepository extends JpaRepository<OrderedProducts, Long> {

}
