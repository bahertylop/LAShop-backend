package org.lashop.newback.dto.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


// используется для добавления пар кроссовок в админской панели
@Data
@Builder
public class SizeQuantity {

    @NotBlank
    private Integer quantity;

    @NotBlank
    private Double size;
}
