package org.lashop.newback.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FavouritesRequest {

    @NotBlank
    private Long accountId;
}
