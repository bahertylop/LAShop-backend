package org.lashop.newback.repositories;

import org.lashop.newback.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Optional<List<Orders>> getOrdersByAccountIdOrderByIdDesc(Long accountId);

    List<Orders> findAllByOrderByIdDesc();
}
