package org.lashop.newback.repositories;

import org.lashop.newback.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


}