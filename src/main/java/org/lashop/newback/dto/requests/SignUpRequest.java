package org.lashop.newback.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequest {

    @Schema(example = "Иван")
    @NotBlank
    private String firstName;

    @Schema(example = "Иванов")
    @NotBlank
    private String lastName;

    @Schema(example = "89003330055")
    @NotBlank
    private String phoneNumber;

    @Schema(example = "qwerty@mail.ru")
    @NotBlank
    private String email;


    @Schema(example = "qwertyuiop")
    @NotBlank
    private String password;
}