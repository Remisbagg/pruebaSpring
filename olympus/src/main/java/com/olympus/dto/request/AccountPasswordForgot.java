package com.olympus.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema
public class AccountPasswordForgot {
    @NotBlank(message = "The email is required")
    @Schema(example = "user@email")
    private String email;
}
