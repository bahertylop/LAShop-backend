package org.lashop.newback.dto.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.lashop.newback.dto.CategoryDto;
import org.lashop.newback.dto.ShoeTypeDto;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class HomeResponse {

    private List<ShoeTypeDto> shoeTypes;

    private List<CategoryDto> categories;
}
