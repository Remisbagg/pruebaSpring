package com.olympus.dto.request;

import com.olympus.validator.annotation.user.ExistByEmailAndPass;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema
@ExistByEmailAndPass
public class AccountLogin {
    @NotBlank
    @Schema(example = "user@email")
    private String email;
    @NotBlank
    @Schema(example = "123456")
    private String password;
}
