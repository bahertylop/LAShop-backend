package org.lashop.newback.repositories;

import org.hibernate.annotations.OptimisticLock;
import org.lashop.newback.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<List<Card>> findAllByAccountId(long accountId);

}

