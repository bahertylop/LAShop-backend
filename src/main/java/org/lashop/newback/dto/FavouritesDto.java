package org.lashop.newback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.Favourite;
import org.lashop.newback.models.ShoeType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavouritesDto {

    private long id;
    private AccountDto account;
    private ShoeTypeDto product;

    public static FavouritesDto from(Favourite favourite) {
        return FavouritesDto.builder()
                .id(favourite.getId())
                .account(AccountDto.from(favourite.getAccount()))
                .product(ShoeTypeDto.from(favourite.getShoeType()))
                .build();
    }

    public static List<FavouritesDto> from(List<Favourite> favourites) {
        return favourites.stream().map(FavouritesDto::from).toList();
    }
}
