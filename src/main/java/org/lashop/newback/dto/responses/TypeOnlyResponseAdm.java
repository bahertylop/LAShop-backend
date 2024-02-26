package org.lashop.newback.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.lashop.newback.dto.ShoeTypeDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TypeOnlyResponseAdm {

    private ShoeTypeDto shoeTypeDto;
    private List<Double> sizes;
}
