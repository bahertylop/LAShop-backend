package org.lashop.newback.dto.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


// используется для добавления пар кроссовок в админской панели
@Data
public class SizeQuantity {

    @NotBlank
    private Integer quantity;

    @NotBlank
    private Double size;
}
