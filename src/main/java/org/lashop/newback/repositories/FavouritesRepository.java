package org.lashop.newback.repositories;

import org.lashop.newback.models.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavouritesRepository extends JpaRepository<Favourite, Long> {
}
