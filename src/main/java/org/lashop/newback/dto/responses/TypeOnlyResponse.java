package org.lashop.newback.dto.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.lashop.newback.dto.ShoeTypeDto;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class TypeOnlyResponse {

    private ShoeTypeDto shoeTypeDto;
    private List<ShoeTypeDto> relatedShoeTypes;
    private List<Double> sizes;
}
