package org.lashop.newback.dto.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
