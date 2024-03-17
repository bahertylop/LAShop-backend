package org.lashop.newback.dto.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentShoe {

    @NotBlank
    private Long shoeTypeId;

    @NotBlank
    private Double size;
}
