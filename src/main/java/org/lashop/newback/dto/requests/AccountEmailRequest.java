package org.lashop.newback.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountEmailRequest {

    @NotBlank
    private String email;
}
