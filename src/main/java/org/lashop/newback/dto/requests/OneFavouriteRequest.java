package org.lashop.newback.dto.requests;

import lombok.Data;

@Data
public class OneFavouriteRequest {

    private Long accountId;
    private Long shoeTypeId;
}