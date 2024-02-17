package org.lashop.newback.dto.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
