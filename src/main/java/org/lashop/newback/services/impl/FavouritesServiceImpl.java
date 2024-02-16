package org.lashop.newback.services.impl;

import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.ShoeTypeDto;
import org.lashop.newback.models.Favourite;
import org.lashop.newback.repositories.AccountRepository;
import org.lashop.newback.repositories.FavouritesRepository;
import org.lashop.newback.repositories.ShoeTypeRepository;
import org.lashop.newback.services.FavouritesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavouritesServiceImpl implements FavouritesService {

    private final FavouritesRepository favouritesRepository;

    @Override
    public List<ShoeTypeDto> getFavouritesList(long accountId) {
        return favouritesRepository.findAllByAccountId(accountId)
                .stream()
                .map(Favourite::getShoeType)
                .map(ShoeTypeDto::from)
                .toList();
    }

    @Override
    public void deleteFavourites(long accountId) {
        favouritesRepository.deleteAllByAccountId(accountId);
    }


    private final AccountRepository accountRepository;
    private final ShoeTypeRepository shoeTypeRepository;
    @Override
    public void addShoeTypeToFavourites(long accountId, long shoeTypeId) {
        Optional<Favourite> position = favouritesRepository.findByAccountIdAndShoeTypeId(accountId, shoeTypeId);

        if (position.isEmpty()) {
            Favourite favourite = Favourite.builder()
                    .account(accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("account not found")))
                    .shoeType(shoeTypeRepository.findById(shoeTypeId).orElseThrow(() -> new RuntimeException("shoeType not found")))
                    .build();

            favouritesRepository.save(favourite);
        }
    }

    @Override
    public void deleteFromFavourites(long accountId, long productId) {
        favouritesRepository.deleteByAccountIdAndShoeTypeId(accountId, productId);
    }
}
