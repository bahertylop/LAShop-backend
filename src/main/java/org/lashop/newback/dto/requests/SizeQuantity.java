package org.lashop.newback.dto.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// используется для добавления пар кроссовок в админской панели
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SizeQuantity {

    @NotBlank
    private Integer quantity;

    @NotBlank
    private Double size;
}
