package org.lashop.newback.dto.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CurrentShoe {

    @NotBlank
    private Long shoeTypeId;

    @NotBlank
    private Double size;
}
