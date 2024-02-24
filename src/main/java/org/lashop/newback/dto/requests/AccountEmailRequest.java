package org.lashop.newback.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountIdRequest {

    @NotBlank
    private Long accountId;
}
