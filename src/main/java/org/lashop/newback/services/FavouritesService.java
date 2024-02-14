package org.lashop.newback.services;

import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.ShoeType;

import java.util.List;

public interface FavouritesService {

    List<ShoeTypeDto> getFavouritesList(long accountId);

    void deleteFavourites(long accountId);

    void addShoeTypeToFavourites(long accountId, long productId);

    void deleteFromFavourites(long accountId, long productId);
}
