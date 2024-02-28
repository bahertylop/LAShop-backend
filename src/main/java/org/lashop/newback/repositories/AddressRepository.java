package org.lashop.newback.repositories;

import org.lashop.newback.models.Address;
import org.lashop.newback.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
